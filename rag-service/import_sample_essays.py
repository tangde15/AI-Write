"""
导入范文数据到 Milvus
支持从 MySQL 读取或直接导入示例数据
依赖版本：pymilvus 2.6.2, FlagEmbedding 1.2.10
"""
import os
import sys
from dotenv import load_dotenv
from pymilvus import connections, Collection
import requests
import json
from typing import List, Dict
import time

# 加载环境变量
load_dotenv('config.env')

MILVUS_HOST = os.getenv('MILVUS_HOST', '127.0.0.1')
MILVUS_PORT = os.getenv('MILVUS_PORT', '19530')
MILVUS_DATABASE = os.getenv('MILVUS_DATABASE', 'Write')
COLLECTION_NAME = 'sample_essay_vectors'

# Embedding API 配置
EMBEDDING_API_KEY = os.getenv('SILICONFLO_API_KEY')
EMBEDDING_API_URL = os.getenv('SILICONFLO_BASE_URL', 'https://api.siliconflow.cn/v1')
EMBEDDING_MODEL = os.getenv('SILICONFLO_EMBEDDING_MODEL', 'BAAI/bge-m3')

# 示例范文数据（生产环境从数据库读取）
SAMPLE_ESSAYS = [
    {
        "id": "essay_001",
        "title": "春天的校园",
        "content": "春天来了，校园里的樱花开了。阳光透过粉色的花瓣洒在操场上，暖洋洋的。同学们在树下读书、嬉戏，笑声像风铃一样清脆。我沿着跑道慢跑，耳边是风的呼呼声和小鸟的叽喳声，闻到空气中淡淡的花香。春天的校园，充满了生机与希望。",
        "grade": "小学三年级",
        "topic": "校园写景"
    },
    {
        "id": "essay_002",
        "title": "难忘的运动会",
        "content": "运动会那天，我参加了接力赛。哨声响起的瞬间，我的心跳得像擂鼓一样快。我紧紧握着接力棒，像离弦的箭一样冲了出去。风在耳边呼啸，同学们的加油声此起彼伏。当我把棒传给下一位队友时，手心全是汗。虽然最后我们只得了第三名，但这次经历让我懂得了团队合作的重要性。",
        "grade": "小学四年级",
        "topic": "叙事记叙"
    },
    {
        "id": "essay_003",
        "title": "窗外的雨",
        "content": "夜里下起了雨，雨滴敲打着窗户，发出滴滴答答的声音，像是在弹奏一首轻柔的夜曲。我坐在窗边，看着雨珠沿着玻璃滑落，留下一道道细细的水痕。空气中弥漫着泥土的清香，偶尔传来几声蛙鸣。这样的雨夜，让人感到宁静而温暖。",
        "grade": "小学五年级",
        "topic": "写景抒情"
    },
    {
        "id": "essay_004",
        "title": "我的好朋友",
        "content": "小明是我最好的朋友。他有一双明亮的大眼睛，笑起来眼睛会眯成两条缝。他特别爱帮助人，有一次我忘带文具盒，他毫不犹豫地把自己的铅笔借给我。他还很幽默，总能用几句玩笑话逗得大家哈哈大笑。和他在一起，我总是很开心。",
        "grade": "小学三年级",
        "topic": "写人记事"
    },
    {
        "id": "essay_005",
        "title": "家乡的小河",
        "content": "家乡的小河清澈见底，河水哗哗地流淌着，像一条银色的绸带。河边的柳树垂下细长的枝条，轻轻拂过水面，激起一圈圈涟漪。夏天的时候，小鱼在水中自由自在地游来游去，偶尔跃出水面，溅起朵朵水花。我常常在河边玩耍，听着水声，闻着青草的香味，感觉无比惬意。",
        "grade": "小学四年级",
        "topic": "写景状物"
    }
]

def get_embedding_via_api(text: str) -> List[float]:
    """通过 API 获取文本向量"""
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
        print(f"❌ 向量化失败: {e}")
        raise

def import_essays(essays: List[Dict]):
    """导入范文到 Milvus"""
    try:
        # 连接 Milvus
        print(f"正在连接 Milvus ({MILVUS_HOST}:{MILVUS_PORT})...")
        connections.connect(
            alias="default",
            host=MILVUS_HOST,
            port=MILVUS_PORT,
            db_name=MILVUS_DATABASE
        )
        
        collection = Collection(COLLECTION_NAME)
        print(f"✅ 连接成功，当前数据量: {collection.num_entities}")
        
        # 批量向量化与插入
        ids = []
        embeddings = []
        titles = []
        grades = []
        topics = []
        
        print(f"\n开始向量化 {len(essays)} 篇范文...")
        for i, essay in enumerate(essays, 1):
            print(f"[{i}/{len(essays)}] 处理: {essay['title']}")
            
            # 拼接完整文本（标题+内容，提升检索效果）
            full_text = f"{essay['title']}\n{essay['content']}"
            
            # 获取向量
            embedding = get_embedding_via_api(full_text)
            
            ids.append(essay['id'])
            embeddings.append(embedding)
            titles.append(essay['title'])
            grades.append(essay.get('grade', '通用'))
            topics.append(essay.get('topic', '记叙文'))
            
            time.sleep(0.5)  # 避免API限流
        
        # 插入 Milvus
        print("\n正在插入 Milvus...")
        entities = [
            ids,
            embeddings,
            titles,
            grades,
            topics
        ]
        collection.insert(entities)
        collection.flush()
        
        print(f"✅ 成功导入 {len(essays)} 篇范文")
        print(f"📊 当前总数据量: {collection.num_entities}")
        
    except Exception as e:
        print(f"❌ 导入失败: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)
    finally:
        connections.disconnect("default")

if __name__ == "__main__":
    print("=" * 60)
    print("范文数据导入工具")
    print("=" * 60)
    
    if not EMBEDDING_API_KEY:
        print("❌ 错误: 未配置 SILICONFLO_API_KEY")
        print("   请在 config.env 中设置 API 密钥")
        sys.exit(1)
    
    print(f"\n使用示例数据: {len(SAMPLE_ESSAYS)} 篇范文")
    print("如需从数据库导入，请修改 SAMPLE_ESSAYS 为数据库查询结果\n")
    
    import_essays(SAMPLE_ESSAYS)
