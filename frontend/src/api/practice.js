import api from './auth';

// ==================== 题库相关 ====================

/**
 * 获取题库列表
 * @param {Object} params - 查询参数
 * @param {string} params.keyword - 搜索关键词
 * @param {string} params.filter - 筛选条件
 * @param {number} params.page - 页码
 * @param {number} params.size - 每页数量
 * @returns {Promise} 题库列表数据
 */
export const getPracticeLibraries = (params) => {
  return api.get('/practice/libraries', { params });
};

/**
 * 获取题库下的练习册列表
 * @param {number} libraryId - 题库ID
 * @returns {Promise} 练习册列表数据
 */
export const getPracticeLibraryBooks = (libraryId) => {
  return api.get(`/practice/libraries/${libraryId}/books`);
};

// ==================== 题单相关 ====================

/**
 * 获取题单列表
 * @param {Object} params - 查询参数
 * @param {string} params.keyword - 搜索关键词
 * @param {string} params.filter - 筛选条件
 * @param {number} params.page - 页码
 * @param {number} params.size - 每页数量
 * @returns {Promise} 题单列表数据
 */
export const getPracticeSets = (params) => {
  return api.get('/practice/sets', { params });
};

/**
 * 获取题单下的练习册列表
 * @param {number} setId - 题单ID
 * @returns {Promise} 练习册列表数据
 */
export const getPracticeSetBooks = (setId) => {
  return api.get(`/practice/sets/${setId}/books`);
};

// ==================== 练习册相关 ====================

/**
 * 获取练习册详情
 * @param {number} bookId - 练习册ID
 * @returns {Promise} 练习册详情数据
 */
export const getPracticeBookDetail = (bookId) => {
  return api.get(`/practice/books/${bookId}`);
};

/**
 * 获取练习册的题目列表
 * @param {number} bookId - 练习册ID
 * @returns {Promise} 题目列表数据
 */
export const getPracticeBookQuestions = (bookId) => {
  return api.get(`/practice/books/${bookId}/questions`);
};

/**
 * 获取练习册的时间线数据（答题进度）
 * @param {number} bookId - 练习册ID
 * @returns {Promise} 时间线数据
 */
export const getPracticeBookTimeline = (bookId) => {
  return api.get(`/practice/books/${bookId}/timeline`);
};

// ==================== 练习题相关 ====================

/**
 * 获取练习题详情
 * @param {number} questionId - 题目ID
 * @returns {Promise} 题目详情数据
 */
export const getPracticeQuestionDetail = (questionId) => {
  return api.get(`/practice/questions/${questionId}`);
};

/**
 * 提交练习题作答
 * @param {number} questionId - 题目ID
 * @param {Object} data - 提交数据
 * @param {string} data.answer - 学生作答内容
 * @returns {Promise} 提交结果
 */
export const submitPracticeAnswer = (questionId, data) => {
  return api.post(`/practice/questions/${questionId}/submit`, data);
};

/**
 * 保存练习题作答草稿
 * @param {number} questionId - 题目ID
 * @param {Object} data - 保存数据
 * @param {string} data.answer - 学生作答内容
 * @returns {Promise} 保存结果
 */
export const savePracticeAnswerDraft = (questionId, data) => {
  return api.post(`/practice/questions/${questionId}/save`, data);
};

/**
 * 获取练习题的系统批改结果
 * @param {number} questionId - 题目ID
 * @returns {Promise} 系统批改数据
 */
export const getPracticeSystemFeedback = (questionId) => {
  return api.get(`/practice/questions/${questionId}/system-feedback`);
};

/**
 * 获取答案状态（轮询用）
 * @param {number} answerId - 答案ID
 * @returns {Promise} 答案状态
 */
export const getPracticeAnswerStatus = (answerId) => {
  return api.get(`/practice/answers/${answerId}/status`);
};

/**
 * 获取答案详情（含题目、AI与教师批改文本等）
 * @param {number} answerId - 答案ID
 * @returns {Promise} 答案详情
 */
export const getPracticeAnswerDetail = (answerId) => {
  return api.get(`/practice/answers/${answerId}/detail`);
};

/**
 * 获取练习题的教师批改结果
 * @param {number} questionId - 题目ID
 * @returns {Promise} 教师批改数据
 */
export const getPracticeTeacherFeedback = (questionId) => {
  return api.get(`/practice/questions/${questionId}/teacher-feedback`);
};

/**
 * 收藏/取消收藏练习题
 * @param {number} questionId - 题目ID
 * @returns {Promise} 操作结果
 */
export const togglePracticeFavorite = (questionId) => {
  return api.post(`/practice/questions/${questionId}/favorite`);
};
