#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""添加单篇作文到 Milvus 知识库"""

import os
from dotenv import load_dotenv
from pymilvus import connections, Collection
import requests
import json
from typing import List
import time
import uuid

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

def add_essay_to_milvus(title: str, content: str, topic: str = "人物描写"):
    """
    添加作文到 Milvus
    
    Args:
        title: 作文标题
        content: 作文内容
        topic: 作文主题
    """
    try:
        # 连接 Milvus
        print(f"连接 Milvus ({MILVUS_HOST}:{MILVUS_PORT})...")
        connections.connect(
            alias="default",
            host=MILVUS_HOST,
            port=MILVUS_PORT,
            db_name=MILVUS_DATABASE
        )
        
        collection = Collection(COLLECTION_NAME)
        print(f"✅ Milvus 连接成功，当前数据量: {collection.num_entities}")
        
        print(f"\n正在添加作文: {title}")
        print(f"字数: {len(content)}")
        
        # 生成向量
        full_text = f"{title}\n{content}"
        embedding = get_embedding_via_api(full_text)
        
        # 生成唯一 ID
        essay_id = f"essay_{int(time.time() * 1000)}"
        
        # 插入 Milvus
        entities = [
            [essay_id],
            [embedding],
            [title],
            ["八年级"],
            [topic]
        ]
        collection.insert(entities)
        collection.flush()
        
        print(f"\n✅ 作文已成功添加到知识库")
        print(f"   ID: {essay_id}")
        print(f"   标题: {title}")
        print(f"   字数: {len(content)}")
        print(f"   主题: {topic}")
        print(f"📊 当前总数据量: {collection.num_entities}")
        
        return {
            "id": essay_id,
            "title": title,
            "word_count": len(content)
        }
    
    except Exception as e:
        print(f"❌ 添加失败: {str(e)}")
        import traceback
        traceback.print_exc()
        raise
    finally:
        connections.disconnect("default")


if __name__ == "__main__":
    if not EMBEDDING_API_KEY:
        print("❌ 错误: 未配置 SILICONFLO_API_KEY")
        print("   请在 config.env 中设置 API 密钥")
        exit(1)
    
    essay_title = "我的妈妈"
    essay_content = """我的妈妈中等身材，眼角的细纹像被时光精心绣上的纹路，那是岁月沉淀的温柔。她总爱把乌黑的长发松松挽成一个发髻，几缕碎发垂在脸颊旁，笑起来时，眼角的纹路会轻轻聚拢，像盛着满眶的暖意。妈妈的手不像别人的那样纤细白皙，指腹带着常年做家务留下的薄茧，掌心却永远带着让人安心的温度，每次握住我的手，都像握住了整个春天。
妈妈的厨艺是家里最温暖的味道。每天清晨，天还没亮，厨房就会传来轻微的声响。她总说外面的早餐不健康，坚持每天为我做营养搭配的早饭。记得有一次我随口说想吃小时候常吃的荠菜饺子，没想到周末回家，餐桌上就摆着一大盘热气腾腾的饺子。翠绿的荠菜混着鲜美的肉馅，咬一口汤汁四溢。我后来才知道，为了挖到新鲜的荠菜，妈妈凌晨五点就去了郊外的田野，回来后又摘洗、剁馅、包饺子，忙了整整一上午。看着她额角的汗珠和沾着面粉的双手，我忽然明白，妈妈的爱，都藏在这些不起眼的细节里。
妈妈不仅在生活上照顾我，更教会我勇敢面对困难。上学期我参加学校的演讲比赛，因为紧张，彩排时多次忘词，回到家后我哭着说不想参加了。妈妈没有责备我，而是坐在我身边，轻轻拍着我的后背说："宝贝，敢于站上舞台就是一种胜利，妈妈相信你可以做到。" 接下来的几天，妈妈每天下班回家，都会放下手头的事情，当我的专属听众，帮我纠正发音、调整语速，还告诉我紧张时可以深呼吸放松。比赛那天，妈妈特意请假去现场为我加油，当我看到台下她鼓励的眼神，忽然就不紧张了。最终我虽然只得了三等奖，但妈妈却比我还开心，她抱着我说："你在妈妈心里，就是最棒的。"
妈妈的温柔里也藏着坚持。我小时候特别挑食，只爱吃肉，不爱吃蔬菜。妈妈没有强硬逼迫我，而是变着花样做蔬菜美食，把胡萝卜切成小兔子形状，把西兰花和虾仁一起炒，还会给我讲每种蔬菜的好处。有一次我故意把蔬菜挑出来，妈妈没有生气，只是平静地说："宝贝，身体就像小花园，需要各种营养才能长得茂盛，只吃肉就像只浇一种水，花园里的花是长不好的。" 从那以后，我慢慢尝试着吃蔬菜，也渐渐明白妈妈的良苦用心。
这就是我的妈妈，她没有惊天动地的壮举，却用日复一日的陪伴和关爱，温暖着我的成长之路。她的手或许粗糙，却为我撑起了一片晴空；她的话语或许朴实，却给了我无穷的力量。我爱我的妈妈，这份爱，就像她对我的爱一样，深沉而绵长，永远藏在岁月的褶皱里，温暖我往后的每一个日子。"""
    
    print("=" * 60)
    print("作文添加工具")
    print("=" * 60 + "\n")
    
    add_essay_to_milvus(
        title=essay_title,
        content=essay_content,
        topic="人物描写"
    )
