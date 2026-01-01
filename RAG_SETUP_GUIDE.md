# RAG 功能启动与使用指南

## 一、前置准备

### 1. 确保 Milvus 已启动
```powershell
# 使用 Docker 启动 Milvus（推荐）
docker run -d --name milvus-standalone `
  -p 19530:19530 -p 9091:9091 `
  -v milvus_data:/var/lib/milvus `
  milvusdb/milvus:latest
```

### 2. 配置环境变量
编辑 `core-code-and-deps/config.env`，确保以下配置正确：
```env
# Milvus 配置
MILVUS_HOST=127.0.0.1
MILVUS_PORT=19530
MILVUS_DATABASE=AI

# Embedding API（用于向量化）
SILICONFLO_API_KEY=你的SiliconFlow密钥
SILICONFLO_BASE_URL=https://api.siliconflow.cn/v1
SILICONFLO_EMBEDDING_MODEL=BAAI/bge-m3

# LLM API（用于批改生成）
DEEPSEEK_API_KEY=你的DeepSeek密钥
DEEPSEEK_BASE_URL=https://api.deepseek.com
```

## 二、初始化范文库（仅首次）

### 1. 创建 Milvus Collection
```powershell
cd "E:\Project Practice\Write\core-code-and-deps"
python init_milvus_collection.py
```
预期输出：
```
正在连接 Milvus (127.0.0.1:19530)...
✅ Milvus 连接成功
正在创建 Collection 'sample_essay_vectors'...
正在创建索引...
✅ Collection 'sample_essay_vectors' 创建成功
   - 向量维度: 1024
   - 索引类型: IVF_FLAT
   - 相似度: COSINE
```

### 2. 导入示例范文
```powershell
python import_sample_essays.py
```
预期输出：
```
开始向量化 5 篇范文...
[1/5] 处理: 春天的校园
[2/5] 处理: 难忘的运动会
...
正在插入 Milvus...
✅ 成功导入 5 篇范文
📊 当前总数据量: 5
```

**从数据库批量导入（生产环境）**：
修改 `import_sample_essays.py` 中的 `SAMPLE_ESSAYS`，从 MySQL 查询：
```python
# 示例：从数据库读取
import mysql.connector
conn = mysql.connector.connect(
    host="localhost",
    user="root",
    password="3410043420",
    database="aiwriting"
)
cursor = conn.cursor(dictionary=True)
cursor.execute("SELECT id, title, content, grade, topic FROM sample_essays WHERE is_approved=1")
SAMPLE_ESSAYS = cursor.fetchall()
```

## 三、启动服务

### 终端 1：启动 Python RAG 服务
```powershell
cd "E:\Project Practice\Write\core-code-and-deps"
python rag_service.py
```
预期输出：
```
============================================================
RAG 服务启动中...
Milvus: 127.0.0.1:19530
Collection: sample_essay_vectors
============================================================
INFO:     Started server process [xxxx]
INFO:     Uvicorn running on http://0.0.0.0:8001
连接 Milvus (127.0.0.1:19530)...
✅ Milvus 连接成功，数据量: 5
```

### 终端 2：启动 Spring Boot 后端
```powershell
cd "E:\Project Practice\Write\backend"
.\mvnw.cmd spring-boot:run
```

### 终端 3：启动前端（如需）
```powershell
cd "E:\Project Practice\Write\frontend"
npm run dev
```

## 四、测试 RAG 功能

### 1. 健康检查
```powershell
# 检查 RAG 服务
curl http://localhost:8001/health

# 预期响应
{
  "status": "ok",
  "milvus": "connected",
  "count": 5
}
```

### 2. 测试范文检索
```powershell
curl -X POST http://localhost:8001/api/rag/search `
  -H "Content-Type: application/json" `
  -d '{
    "essay_text": "春天来了，校园里的花开了。",
    "topK": 3
  }'
```

### 3. 测试批改生成（完整 RAG）
```powershell
curl -X POST http://localhost:8001/api/rag/feedback `
  -H "Content-Type: application/json" `
  -d '{
    "essay_text": "春天来了，校园里的花开了。同学们在操场上玩耍。",
    "topic": "春天的校园",
    "requirement": "运用五感描写"
  }'
```

### 4. 通过 Spring Boot 提交作文
前端正常提交作文，后端会自动：
1. 检查 RAG 服务是否可用
2. 如可用，调用 Python RAG 服务（自动检索范文+生成批改）
3. 如不可用，降级为原有批改逻辑

## 五、配置开关

### 关闭 RAG 功能
编辑 `backend/src/main/resources/application.yml`：
```yaml
rag:
  service:
    enabled: false  # 改为 false
```
重启 Spring Boot，系统恢复为普通批改模式。

### 调整检索数量
在 `WritingService.handleRequest()` 中调整 `topK` 参数（默认 3）。

## 六、常见问题

### Q1: Milvus 连接失败
- 检查 Docker 容器是否运行：`docker ps | findstr milvus`
- 检查端口是否开放：`netstat -an | findstr 19530`

### Q2: 向量化超时
- 检查 API 密钥是否正确
- 增加超时时间：修改 `rag_service.py` 中的 `timeout=30` → `timeout=60`

### Q3: RAG 服务启动失败
- 检查依赖版本：`pip list | findstr -i "pymilvus fastapi"`
- 应为：`pymilvus 2.6.2`, `fastapi 0.121.1`

### Q4: Spring Boot 无法调用 RAG
- 检查配置：`rag.service.enabled=true`
- 检查端口：RAG 服务默认 8001，确保未被占用
- 查看日志：Spring Boot 启动时会输出 RAG 健康检查结果

## 七、生产部署建议

1. **范文库持续更新**：定期运行 `import_sample_essays.py` 追加新范文
2. **监控与降级**：RAG 服务异常时自动降级，不影响主业务
3. **性能优化**：
   - Milvus 索引调优：改用 HNSW 提升检索速度
   - 向量缓存：常见作文主题向量预计算
4. **扩展性**：Python 服务可独立扩容（多实例 + 负载均衡）

## 八、验收展示要点

1. **演示检索效果**：提交作文 → 展示返回的 3 条相似范文
2. **对比批改质量**：
   - 关闭 RAG：普通批改
   - 开启 RAG：批改中引用范文片段，建议更具体
3. **系统稳定性**：关闭 Python 服务 → Spring Boot 自动降级，不报错
4. **技术栈体现**：
   - 向量数据库：Milvus
   - 语义检索：BGE Embedding
   - RAG：LangChain Prompt Template
   - 微服务：Python FastAPI + Java Spring Boot
