<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../api/request'


const router = useRouter()

// 表单数据
const username = ref('')
const password = ref('')
const nickname = ref('')
const email = ref('')
const phone = ref('')

// 页面提示信息
const message = ref('')
const messageType = ref('')

// 注册按钮加载状态
const loading = ref(false)

// 注册
async function register() {
  message.value = ''

  if (username.value.trim() === '') {
    message.value = '请输入用户名'
    messageType.value = 'error'
    return
  }

  if (username.value.trim().length < 3) {
    message.value = '用户名至少需要 3 个字符'
    messageType.value = 'error'
    return
  }

  if (password.value.length < 6) {
    message.value = '密码至少需要 6 个字符'
    messageType.value = 'error'
    return
  }

  loading.value = true

  try {
    const response = await request.post('/users/register', {
  username: username.value.trim(),
  password: password.value,
  nickname: nickname.value.trim(),
  email: email.value.trim(),
  phone: phone.value.trim()
})


    if (response.data.code === 200) {
      message.value = '注册成功，即将返回登录页'
      messageType.value = 'success'

      setTimeout(() => {
        router.push('/login')
      }, 800)
    } else {
      message.value = response.data.message || '注册失败'
      messageType.value = 'error'
    }
  } catch (error) {
    console.error('注册请求失败：', error)

    if (error.response && error.response.data) {
      message.value =
        error.response.data.message || '注册失败，请检查输入内容'
    } else {
      message.value = '无法连接后端，请检查 Spring Boot 是否启动'
    }

    messageType.value = 'error'
  } finally {
    loading.value = false
  }
}

// 返回登录页
function goToLogin() {
  router.push('/login')
}
</script>

<template>
  <main class="register-page">
    <section class="register-card">
      <div class="brand">
        <h1>智能购物平台</h1>
        <p>创建新账号</p>
      </div>

      <form @submit.prevent="register">
        <div class="form-item">
          <label for="username">用户名</label>

          <input
            id="username"
            v-model="username"
            type="text"
            placeholder="请输入用户名，至少 3 个字符"
          />
        </div>

        <div class="form-item">
          <label for="password">密码</label>

          <input
            id="password"
            v-model="password"
            type="password"
            placeholder="请输入密码，至少 6 个字符"
          />
        </div>

        <div class="form-item">
          <label for="nickname">昵称</label>

          <input
            id="nickname"
            v-model="nickname"
            type="text"
            placeholder="请输入昵称，可选"
          />
        </div>

        <div class="form-item">
          <label for="email">邮箱</label>

          <input
            id="email"
            v-model="email"
            type="email"
            placeholder="请输入邮箱，可选"
          />
        </div>

        <div class="form-item">
          <label for="phone">手机号</label>

          <input
            id="phone"
            v-model="phone"
            type="text"
            placeholder="请输入手机号，可选"
          />
        </div>

        <button
          class="login-button"
          type="submit"
          :disabled="loading"
        >
          {{ loading ? '注册中...' : '注册' }}
        </button>
      </form>

      <p
        v-if="message"
        class="message"
        :class="messageType"
      >
        {{ message }}
      </p>

      <button
        class="text-button"
        type="button"
        @click="goToLogin"
      >
        已有账号？返回登录
      </button>
    </section>
  </main>
</template>
