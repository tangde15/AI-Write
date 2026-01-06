"""
RAG 微服务 - 范文检索与批改生成
FastAPI 独立服务，供 Spring Boot 主服务调用
端口: 8001
依赖: fastapi 0.121.1, pymilvus 2.6.2, FlagEmbedding 1.2.10
"""
import os
import sys
from dotenv import load_dotenv
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Optional
from pymilvus import connections, Collection
import requests
import logging

# 加载提示词模板
from rag_prompts import ESSAY_CORRECTION_WITH_RAG, format_references

# 加载环境变量
load_dotenv('config.env')

# 日志配置
logging.basicConfig(level=logging.INFO, format='[%(asctime)s] %(levelname)s: %(message)s')
logger = logging.getLogger('RAG_Service')

# 配置
MILVUS_HOST = os.getenv('MILVUS_HOST', '127.0.0.1')
MILVUS_PORT = os.getenv('MILVUS_PORT', '19530')
MILVUS_DATABASE = os.getenv('MILVUS_DATABASE', 'Write')
COLLECTION_NAME = 'sample_essay_vectors'

EMBEDDING_API_KEY = os.getenv('SILICONFLO_API_KEY')
EMBEDDING_API_URL = os.getenv('SILICONFLO_BASE_URL', 'https://api.siliconflow.cn/v1')
EMBEDDING_MODEL = os.getenv('SILICONFLO_EMBEDDING_MODEL', 'BAAI/bge-m3')

LLM_API_KEY = os.getenv('DEEPSEEK_API_KEY')
LLM_API_URL = os.getenv('DEEPSEEK_BASE_URL', 'https://api.deepseek.com')
LLM_MODEL = "deepseek-chat"

app = FastAPI(title="RAG Service", version="1.0.0")

# CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 全局 Milvus 连接
milvus_collection = None

@app.on_event("startup")
async def startup():
    """启动时连接 Milvus"""
    global milvus_collection
    try:
        logger.info(f"连接 Milvus ({MILVUS_HOST}:{MILVUS_PORT})...")
        connections.connect(
            alias="default",
            host=MILVUS_HOST,
            port=MILVUS_PORT,
            db_name=MILVUS_DATABASE
        )
        milvus_collection = Collection(COLLECTION_NAME)
        milvus_collection.load()
        logger.info(f"✅ Milvus 连接成功，数据量: {milvus_collection.num_entities}")
    except Exception as e:
        logger.error(f"❌ Milvus 连接失败: {e}")
        milvus_collection = None

@app.on_event("shutdown")
async def shutdown():
    """关闭时断开 Milvus"""
    try:
        connections.disconnect("default")
        logger.info("Milvus 连接已断开")
    except:
        pass

# ========== 数据模型 ==========
class SearchRequest(BaseModel):
    essay_text: str
    topK: int = 3

class SearchResult(BaseModel):
    id: str
    title: str
    snippet: str
    score: float

class SearchResponse(BaseModel):
    success: bool
    results: List[SearchResult]
    message: Optional[str] = None

class FeedbackRequest(BaseModel):
    essay_text: str
    topic: str = "作文"
    requirement: str = "无特殊要求"
    references: Optional[List[dict]] = None

class FeedbackResponse(BaseModel):
    success: bool
    feedback: str
    message: Optional[str] = None

# ========== 工具函数 ==========
def get_embedding(text: str) -> List[float]:
    """获取文本向量"""
    try:
        headers = {
            "Authorization": f"Bearer {EMBEDDING_API_KEY}",
            "Content-Type": "application/json"
        }
        payload = {
            "model": EMBEDDING_MODEL,
            "input": text,
            "encoding_format": "float"
        }
        response = requests.post(
            f"{EMBEDDING_API_URL}/embeddings",
            headers=headers,
            json=payload,
            timeout=30
        )
        response.raise_for_status()
        result = response.json()
        return result['data'][0]['embedding']
    except Exception as e:
        logger.error(f"向量化失败: {e}")
        raise HTTPException(status_code=500, detail=f"向量化失败: {str(e)}")

def call_llm(prompt: str) -> str:
    """调用 LLM 生成批改"""
    try:
        headers = {
            "Authorization": f"Bearer {LLM_API_KEY}",
            "Content-Type": "application/json"
        }
        payload = {
            "model": LLM_MODEL,
            "messages": [
                {"role": "system", "content": "你是一个作文指导老师，擅长五感训练法"},
                {"role": "user", "content": prompt}
            ],
            "temperature": 0.7
        }
        response = requests.post(
            f"{LLM_API_URL}/chat/completions",
            headers=headers,
            json=payload,
            timeout=60
        )
        response.raise_for_status()
        result = response.json()
        return result['choices'][0]['message']['content']
    except Exception as e:
        logger.error(f"LLM 调用失败: {e}")
        raise HTTPException(status_code=500, detail=f"LLM 调用失败: {str(e)}")

# ========== API 端点 ==========
@app.get("/health")
async def health():
    """健康检查"""
    milvus_ok = milvus_collection is not None
    return {
        "status": "ok" if milvus_ok else "degraded",
        "milvus": "connected" if milvus_ok else "disconnected",
        "count": milvus_collection.num_entities if milvus_ok else 0
    }

@app.post("/api/rag/search", response_model=SearchResponse)
async def search_similar_essays(req: SearchRequest):
    """
    检索相似范文
    输入：学生作文文本
    输出：topK 相似范文（ID+标题+片段+相似度）
    """
    try:
        if not milvus_collection:
            return SearchResponse(
                success=False,
                results=[],
                message="Milvus 未连接，检索服务不可用"
            )
        
        # 向量化查询文本
        logger.info(f"检索请求: topK={req.topK}, 文本长度={len(req.essay_text)}")
        query_vector = get_embedding(req.essay_text)
        
        # Milvus 检索
        search_params = {"metric_type": "COSINE", "params": {"nprobe": 16}}
        results = milvus_collection.search(
            data=[query_vector],
            anns_field="embedding",
            param=search_params,
            limit=req.topK,
            output_fields=["id", "title", "grade", "topic"]
        )
        
        # 格式化结果
        search_results = []
        for hit in results[0]:
            # 生成摘要片段（实际应从数据库读取完整内容）
            snippet = f"年级: {hit.entity.get('grade')}, 主题: {hit.entity.get('topic')}"
            search_results.append(SearchResult(
                id=hit.entity.get('id'),
                title=hit.entity.get('title'),
                snippet=snippet,
                score=float(hit.score)
            ))
        
        logger.info(f"✅ 检索成功，返回 {len(search_results)} 条结果")
        return SearchResponse(success=True, results=search_results)
        
    except Exception as e:
        logger.error(f"检索失败: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/rag/feedback", response_model=FeedbackResponse)
async def generate_feedback(req: FeedbackRequest):
    """
    生成 RAG 批改建议
    输入：学生作文 + 题目 + 要求 + [可选]检索到的参考范文
    输出：完整批改结果（符合 Spring Boot 的输出格式）
    """
    try:
        # 如果未提供参考范文，先检索
        if not req.references:
            search_req = SearchRequest(essay_text=req.essay_text, topK=3)
            search_resp = await search_similar_essays(search_req)
            references = [r.dict() for r in search_resp.results]
        else:
            references = req.references
        
        # 格式化参考范文
        formatted_refs = format_references(references)
        
        # 填充提示词
        prompt = ESSAY_CORRECTION_WITH_RAG.format(
            references=formatted_refs,
            topic=req.topic,
            requirement=req.requirement,
            essay_text=req.essay_text
        )
        
        logger.info(f"生成批改: 题目={req.topic}, 参考数={len(references)}")
        
        # 【后台记录】调用 LLM 前记录本次使用的参考范文
        logger.info(
            "RAG 批改参考范文",
            extra={
                "essay_length": len(req.essay_text),
                "topic": req.topic,
                "references": [
                    {
                        "id": r["id"],
                        "title": r["title"],
                        "score": r["score"]
                    } for r in references
                ]
            }
        )
        
        # 调用 LLM
        feedback = call_llm(prompt)
        
        logger.info(f"✅ 批改生成成功，字数={len(feedback)}")
        return FeedbackResponse(success=True, feedback=feedback)
        
    except Exception as e:
        logger.error(f"批改生成失败: {e}")
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    import uvicorn
    logger.info("=" * 60)
    logger.info("RAG 服务启动中...")
    logger.info(f"Milvus: {MILVUS_HOST}:{MILVUS_PORT}")
    logger.info(f"Collection: {COLLECTION_NAME}")
    logger.info("=" * 60)
    uvicorn.run(app, host="0.0.0.0", port=8001)
