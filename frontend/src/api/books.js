import api from './auth'

export const teacherBooksAPI = {
  create(data) { return api.post('/teacher/books', data) },
  list() { return api.get('/teacher/books') },
  getBook(bookId) { return api.get(`/teacher/books/${bookId}`) },
  update(bookId, data) { return api.put(`/teacher/books/${bookId}`, data) },
  delete(bookId) { return api.delete(`/teacher/books/${bookId}`) },
  questions(bookId) { return api.get(`/teacher/books/${bookId}/questions`) },
  addQuestion(bookId, data) { return api.post(`/teacher/books/${bookId}/questions`, data) },
  deleteQuestion(questionId) { return api.delete(`/teacher/books/questions/${questionId}`) },
  pushPreview(bookId) { return api.get(`/teacher/books/${bookId}/push/preview`) },
  push(bookId, studentIds) { return api.post(`/teacher/books/${bookId}/push`, { studentIds }) },
  getPushRecords(bookId) { return api.get(`/teacher/books/${bookId}/push-records`) }
}

export const studentBooksAPI = {
  pushed() { return api.get('/student/books/pushed') },
  detail(bookId) { return api.get(`/student/books/${bookId}`) }
}
