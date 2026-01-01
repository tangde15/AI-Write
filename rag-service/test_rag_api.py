#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
测试RAG API的脚本
"""

import requests
import json
import time

BASE_URL = "http://localhost:8001"

def test_health():
    """测试服务健康状态"""
    print("=" * 60)
    print("测试1: 服务健康状态")
    print("=" * 60)
    try:
        response = requests.get(f"{BASE_URL}/health", timeout=5)
        print(f"状态码: {response.status_code}")
        print(f"响应: {response.json()}")
        print("✅ 服务健康\n")
        return True
    except Exception as e:
        print(f"❌ 错误: {e}\n")
        return False

def test_search():
    """测试相似文章搜索"""
    print("=" * 60)
    print("测试2: 相似文章搜索")
    print("=" * 60)
    
    payload = {
        "essay_text": "我喜欢春天校园的美景，花儿开了，树也绿了。",
        "topK": 3
    }
    
    try:
        response = requests.post(
            f"{BASE_URL}/api/rag/search",
            json=payload,
            timeout=30
        )
        print(f"状态码: {response.status_code}")
        data = response.json()
        
        if response.status_code == 200:
            print(f"找到 {len(data.get('results', []))} 篇相似文章:")
            for i, result in enumerate(data.get('results', []), 1):
                print(f"\n  [{i}] {result.get('title', 'N/A')}")
                print(f"      相似度: {result.get('score', 0):.4f}")
                print(f"      摘要: {result.get('snippet', 'N/A')[:50]}...")
            print("✅ 搜索成功\n")
        else:
            print(f"❌ 错误: {data}\n")
    except Exception as e:
        print(f"❌ 错误: {e}\n")

def test_feedback():
    """测试RAG生成反馈"""
    print("=" * 60)
    print("测试3: RAG生成反馈")
    print("=" * 60)
    
    payload = {
        "topic": "我的学校生活",
        "requirement": "字数不少于500字，要求内容充实，表达清晰",
        "essay_text": "我的学校生活很充实。每天早上我都会按时到校，认真听课。下午还有各种兴趣活动。我喜欢学校的生活。"
    }
    
    try:
        response = requests.post(
            f"{BASE_URL}/api/rag/feedback",
            json=payload,
            timeout=60
        )
        print(f"状态码: {response.status_code}")
        data = response.json()
        
        if response.status_code == 200:
            feedback = data.get('feedback', '')
            print(f"\n生成的反馈:\n{feedback}\n")
            print("✅ 反馈生成成功\n")
        else:
            print(f"❌ 错误: {data}\n")
    except Exception as e:
        print(f"❌ 错误: {e}\n")

if __name__ == "__main__":
    print("\n🚀 开始测试RAG API服务\n")
    
    # 先测试健康状态
    if not test_health():
        print("❌ 服务未运行，请先启动RAG服务")
        exit(1)
    
    time.sleep(1)
    
    # 测试搜索
    test_search()
    
    time.sleep(1)
    
    # 测试反馈
    test_feedback()
    
    print("=" * 60)
    print("✅ 所有测试完成")
    print("=" * 60)
