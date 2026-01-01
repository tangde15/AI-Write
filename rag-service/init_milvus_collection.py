"""
初始化 Milvus 范文向量库 Collection
基于 core-code-and-deps 的依赖版本：pymilvus 2.6.2
"""
import os
import sys
from dotenv import load_dotenv
from pymilvus import connections, Collection, FieldSchema, CollectionSchema, DataType, utility

# 加载环境变量
load_dotenv('config.env')

MILVUS_HOST = os.getenv('MILVUS_HOST', '127.0.0.1')
MILVUS_PORT = os.getenv('MILVUS_PORT', '19530')
MILVUS_DATABASE = os.getenv('MILVUS_DATABASE', 'Write')
COLLECTION_NAME = 'sample_essay_vectors'
EMBEDDING_DIM = 1024  # BAAI/bge-m3 向量维度

def init_collection():
    """创建 Milvus Collection（如已存在则跳过）"""
    try:
        # 先连接默认数据库
        print(f"正在连接 Milvus ({MILVUS_HOST}:{MILVUS_PORT})...")
        connections.connect(
            alias="default",
            host=MILVUS_HOST,
            port=MILVUS_PORT
        )
        print("✅ Milvus 连接成功")
        
        # 创建数据库（如果不存在）
        from pymilvus import db
        databases = db.list_database()
        if MILVUS_DATABASE not in databases:
            print(f"创建数据库 '{MILVUS_DATABASE}'...")
            db.create_database(MILVUS_DATABASE)
            print(f"✅ 数据库 '{MILVUS_DATABASE}' 创建成功")
        else:
            print(f"数据库 '{MILVUS_DATABASE}' 已存在")
        
        # 切换到目标数据库
        db.using_database(MILVUS_DATABASE)
        
        # 检查 collection 是否已存在
        if utility.has_collection(COLLECTION_NAME):
            print(f"⚠️ Collection '{COLLECTION_NAME}' 已存在，跳过创建")
            coll = Collection(COLLECTION_NAME)
            print(f"📊 当前数据量: {coll.num_entities} 条")
            return
        
        # 定义 schema
        fields = [
            FieldSchema(name="id", dtype=DataType.VARCHAR, is_primary=True, max_length=100),
            FieldSchema(name="embedding", dtype=DataType.FLOAT_VECTOR, dim=EMBEDDING_DIM),
            FieldSchema(name="title", dtype=DataType.VARCHAR, max_length=200),
            FieldSchema(name="grade", dtype=DataType.VARCHAR, max_length=50),
            FieldSchema(name="topic", dtype=DataType.VARCHAR, max_length=200),
        ]
        schema = CollectionSchema(fields, description="范文向量库")
        
        # 创建 collection
        print(f"正在创建 Collection '{COLLECTION_NAME}'...")
        collection = Collection(COLLECTION_NAME, schema)
        
        # 创建索引（IVF_FLAT，适合中小规模）
        index_params = {
            "metric_type": "COSINE",  # 余弦相似度
            "index_type": "IVF_FLAT",
            "params": {"nlist": 128}
        }
        print("正在创建索引...")
        collection.create_index(field_name="embedding", index_params=index_params)
        
        # 加载到内存
        collection.load()
        
        print(f"✅ Collection '{COLLECTION_NAME}' 创建成功")
        print(f"   - 向量维度: {EMBEDDING_DIM}")
        print(f"   - 索引类型: IVF_FLAT")
        print(f"   - 相似度: COSINE")
        
    except Exception as e:
        print(f"❌ 初始化失败: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)
    finally:
        connections.disconnect("default")

if __name__ == "__main__":
    init_collection()
