import api from './auth'

export const sampleEssayAPI = {
  // 获取所有范文（每日推荐）
  getAllEssays() {
    return api.get('/sample-essays')
  },

  // 根据ID获取范文详情
  getEssayById(id) {
    return api.get(`/sample-essays/${id}`)
  },

  // 根据标签获取范文
  getEssaysByTag(tag) {
    return api.get(`/sample-essays/tag/${tag}`)
  },

  // 获取收藏榜单（收藏数最高的范文）
  getTopFavoriteEssays() {
    return api.get('/sample-essays/top-favorites')
  },

  // 增加收藏数
  incrementFavorite(id) {
    return api.post(`/sample-essays/${id}/favorite`)
  }
}


