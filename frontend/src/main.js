//注册 Vue Router

import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'

const app = createApp(App)

//把刚刚创建的路由功能安装到 Vue 应用中
//如果不写这句，即使 router/index.js 配置正确，Vue 也不会使用路由。
app.use(router)

app.mount('#app')
