//用户登录页面
<script setup>
import { ref } from 'vue'
import request from '../api/request'

import { useRouter } from 'vue-router'

// 保存用户名输入框中的内容
const username = ref('')

// 保存密码输入框中的内容
const password = ref('')

// 保存页面提示信息
const message = ref('')

// 保存提示信息的类型
const messageType = ref('')

// 保存路由对象
const router = useRouter()


// 保存登录按钮的加载状态
const loading = ref(false)



// 点击登录按钮时执行
async function login() {
  // 清空之前的提示信息
  message.value = ''

  // 判断用户名是否为空
  if (username.value.trim() === '') {
    message.value = '请输入用户名'
    messageType.value = 'error'
    return
  }

  // 判断密码是否为空
  if (password.value.trim() === '') {
    message.value = '请输入密码'
    messageType.value = 'error'
    return
  }

  // 开始请求后端时，按钮进入加载状态
  loading.value = true

  try {
    // 调用 Spring Boot 登录接口
    const response = await request.post('/users/login', {
  username: username.value,
  password: password.value
})


    // 读取后端返回的 Result.code
    if (response.data.code === 200) {
      message.value = '登录成功'
      messageType.value = 'success'

      console.log('登录用户信息：', response.data.data)

      const redirectPath =
        router.currentRoute.value.query.redirect || '/products'

      setTimeout(() => {
        router.push(redirectPath)
      }, 500)
    }
    else {
      message.value = response.data.message
      messageType.value = 'error'
    }

  } catch (error) {
  console.error('登录请求失败：', error)

  if (error.response) {
    message.value =
      error.response.data?.message ||
      `登录失败，HTTP 状态码：${error.response.status}`
  } else if (error.request) {
    message.value =
      '请求未收到响应，请检查网关、跨域配置或网络连接'
  } else {
    message.value = '登录请求配置错误'
  }

  messageType.value = 'error'
}
finally {
    // 无论成功还是失败，都结束加载状态
    loading.value = false
  }
}
</script>

<template>
  <main class="login-page">
    <section class="login-card">
      <div class="brand">
        <h1>智能购物平台</h1>
        <p>欢迎登录</p>
      </div>

      <form @submit.prevent="login">
        <div class="form-item">
          <label for="username">用户名</label>

          <input
            id="username"
            v-model="username"
            type="text"
            placeholder="请输入用户名"
          />
        </div>

        <div class="form-item">
          <label for="password">密码</label>

          <input
            id="password"
            v-model="password"
            type="password"
            placeholder="请输入密码"
          />
        </div>

        <button
          class="login-button"
          type="submit"
          :disabled="loading"
        >
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </form>

      <p
        v-if="message"
        class="message"
        :class="messageType"
      >
        {{ message }}
      </p>

      <p class="register-tip">
  还没有账号？

  <button
    class="text-button"
    type="button"
    @click="router.push('/register')"
  >
    注册新用户
  </button>
</p>

    </section>
  </main>
</template>
