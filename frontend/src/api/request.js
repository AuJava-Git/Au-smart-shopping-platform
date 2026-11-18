import axios from 'axios'

const request = axios.create({
  // Gateway 的业务路由统一以 /api 开头，例如 /api/users/login
  // 页面中继续使用 /users/login、/products 等相对业务路径即可
  baseURL: 'http://localhost:9000/api',
  timeout: 10000,
  withCredentials: true
})

request.interceptors.response.use(
  response => {
    return response
  },
  error => {
    console.error('接口请求失败：', error)
    return Promise.reject(error)
  }
)

export default request
