import axios from 'axios';

const API_BASE_URL = '/api/friends';

// 配置axios实例，携帧Cookie
const api = axios.create({
  withCredentials: true
});

export default {
  /**
   * 搜索用户（通过账号）
   */
  searchUser(account) {
    return api.get(`${API_BASE_URL}/search`, {
      params: { account }
    });
  },

  /**
   * 发送好友请求
   */
  sendFriendRequest(friendId) {
    return api.post(`${API_BASE_URL}/request`, { friendId });
  },

  /**
   * 获取待处理的好友请求
   */
  getFriendRequests() {
    return api.get(`${API_BASE_URL}/requests`);
  },

  /**
   * 接受好友请求
   */
  acceptFriendRequest(relationId) {
    return api.post(`${API_BASE_URL}/accept/${relationId}`);
  },

  /**
   * 拒绝好友请求
   */
  rejectFriendRequest(relationId) {
    return api.post(`${API_BASE_URL}/reject/${relationId}`);
  },

  /**
   * 获取好友列表
   */
  getFriendList() {
    return api.get(`${API_BASE_URL}/list`);
  },

  /**
   * 删除好友
   */
  deleteFriend(friendId) {
    return api.delete(`${API_BASE_URL}/${friendId}`);
  }
};
