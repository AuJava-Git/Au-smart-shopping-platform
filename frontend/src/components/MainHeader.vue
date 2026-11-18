<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../api/request'
import { clearCurrentUser } from '../router'

const router = useRouter()
const currentUser = ref(null)

async function loadCurrentUser() {
  try {
    const response = await request.get('/users/me')

    if (
      response.data.code === 200 &&
      response.data.data
    ) {
      currentUser.value = response.data.data
    } else {
      currentUser.value = null
    }
  } catch (error) {
    // 未登录时后端会返回 401，这属于正常情况，
    // 不需要把它当成系统错误显示给用户。
    if (error.response?.status !== 401) {
      console.error('获取当前用户失败：', error)
    }

    currentUser.value = null
  }
}


function go(path) {
  router.push(path)
}

async function logout() {
  try {
    const response = await request.post('/users/logout', {})
    if (response.data.code === 200) {
      clearCurrentUser()
      currentUser.value = null
      await router.push('/login')
    } else {
      alert(response.data.message || '退出登录失败')
    }
  } catch (error) {
    console.error('退出登录失败：', error)
    alert(error.response?.data?.message || '退出登录失败，请稍后重试')
  }
}

onMounted(loadCurrentUser)
</script>

<template>
  <header class="main-header">
    <div class="header-inner">
      <button class="brand-button" type="button" @click="go('/products')">
        智能购物平台
      </button>

      <nav class="header-nav">
        <button type="button" @click="go('/products')">商品列表</button>
        <button type="button" @click="go('/cart')">购物车</button>
        <button type="button" @click="go('/orders')">我的订单</button>
        <button type="button" @click="go('/features')">会员服务</button>
        <button type="button" @click="go('/chat')">智能客服</button>
        <button
          v-if="currentUser && Number(currentUser.role) === 1"
          type="button"
          @click="go('/admin/products')"
        >商品管理</button>
        <button
          v-if="currentUser && Number(currentUser.role) === 1"
          type="button"
          @click="go('/admin/orders')"
        >订单管理</button>
        <button v-if="currentUser && Number(currentUser.role) === 1" type="button" @click="go('/admin/categories')">分类管理</button>
        <button v-if="currentUser && Number(currentUser.role) === 1" type="button" @click="go('/admin/reviews')">评论审核</button>
      </nav>

      <div class="header-user">
  <!-- 已登录：显示用户信息 -->
  <button
    v-if="currentUser"
    type="button"
    @click="go('/user-center')"
  >
    欢迎，{{ currentUser.nickname || currentUser.username }}
  </button>

  <!-- 未登录：显示请登录 -->
  <button
    v-else
    type="button"
    @click="go('/login')"
  >
    请登录
  </button>

  <!-- 只有已登录时才显示退出登录 -->
  <button
    v-if="currentUser"
    class="logout-button"
    type="button"
    @click="logout"
  >
    退出登录
  </button>
</div>

    </div>
  </header>
</template>
