<script setup>
import { onMounted, reactive, ref } from 'vue'
import request from '../api/request'
import { useRouter } from 'vue-router'

const router = useRouter()

const favorites = ref([])
const favoritesLoading = ref(false)
const user = ref(null)
const loading = ref(false)
const savingProfile = ref(false)
const savingPassword = ref(false)
const message = ref('')
const messageType = ref('')
const profileForm = reactive({ nickname: '', email: '', phone: '' })
const passwordForm = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })

function showMessage(text, type = 'success') {
  message.value = text
  messageType.value = type
}

function fillForm(data) {
  user.value = data
  profileForm.nickname = data.nickname || ''
  profileForm.email = data.email || ''
  profileForm.phone = data.phone || ''
}

async function loadUserInfo() {
  loading.value = true
  try {
    const response = await request.get('/users/me')
    if (response.data.code === 200) fillForm(response.data.data)
    else showMessage(response.data.message || '获取用户信息失败', 'error')
  } catch (error) {
    showMessage(error.response?.data?.message || '获取用户信息失败，请稍后重试', 'error')
  } finally { loading.value = false }
}

async function saveProfile() {
  savingProfile.value = true
  try {
    const response = await request.put('/users/profile', { ...profileForm })
    if (response.data.code === 200) {
      fillForm(response.data.data)
      showMessage('个人资料保存成功')
    } else showMessage(response.data.message || '保存失败', 'error')
  } catch (error) { showMessage(error.response?.data?.message || '保存失败，请稍后重试', 'error') }
  finally { savingProfile.value = false }
}

async function changePassword() {
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    showMessage('两次输入的新密码不一致', 'error'); return
  }
  savingPassword.value = true
  try {
    const response = await request.put('/users/password', {
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })
    if (response.data.code === 200) {
      passwordForm.oldPassword = ''; passwordForm.newPassword = ''; passwordForm.confirmPassword = ''
      showMessage('密码修改成功，请牢记新密码')
    } else showMessage(response.data.message || '密码修改失败', 'error')
  } catch (error) { showMessage(error.response?.data?.message || '密码修改失败，请稍后重试', 'error') }
  finally { savingPassword.value = false }
}

async function loadFavorites() {
  favoritesLoading.value = true

  try {
    const response = await request.get('/favorites')

    if (response.data.code === 200) {
      favorites.value = response.data.data || []
    }
  } catch (error) {
    showMessage(
      error.response?.data?.message ||
      '获取收藏列表失败',
      'error'
    )
  } finally {
    favoritesLoading.value = false
  }
}

async function removeFavorite(productId) {
  try {
    const response = await request.delete(
      `/favorites/${productId}`
    )

    if (response.data.code === 200) {
      favorites.value = favorites.value.filter(
        item => item.productId !== productId
      )

      showMessage('已取消收藏')
    } else {
      showMessage(
        response.data.message || '取消收藏失败',
        'error'
      )
    }
  } catch (error) {
    showMessage(
      error.response?.data?.message ||
      '取消收藏失败',
      'error'
    )
  }
}



onMounted(() => {
  loadUserInfo()
  loadFavorites()
})
</script>

<template>
  <main class="user-center-page page-shell">
    <header class="page-heading">
      <div><div class="section-kicker">ACCOUNT CENTER</div><h1>个人中心</h1><p>管理你的账户资料和登录安全</p></div>
    </header>
    <p v-if="message" class="notice" :class="messageType">{{ message }}</p>
    <p v-if="loading" class="state-text">用户信息加载中...</p>
    <div v-else-if="user" class="account-grid">
      <section class="user-center-card">
        <div class="card-title"><div><span class="muted-label">PROFILE</span><h2>基本资料</h2></div><span class="role-badge">{{ Number(user.role) === 1 ? '管理员' : '普通用户' }}</span></div>
        <div class="user-info-item readonly"><span>用户名</span><strong>{{ user.username }}</strong></div>
        <form class="profile-form" @submit.prevent="saveProfile">
          <label>昵称<input v-model.trim="profileForm.nickname" maxlength="50" placeholder="请输入昵称" /></label>
          <label>邮箱<input v-model.trim="profileForm.email" type="email" maxlength="100" placeholder="name@example.com" /></label>
          <label>手机号<input v-model.trim="profileForm.phone" maxlength="20" placeholder="请输入手机号" /></label>
          <button class="primary-button" type="submit" :disabled="savingProfile">{{ savingProfile ? '保存中...' : '保存资料' }}</button>
        </form>
      </section>
      <section class="user-center-card">
        <div class="card-title"><div><span class="muted-label">SECURITY</span><h2>修改密码</h2></div></div>
        <form class="profile-form" @submit.prevent="changePassword">
          <label>原密码<input v-model="passwordForm.oldPassword" type="password" minlength="6" required placeholder="请输入原密码" /></label>
          <label>新密码<input v-model="passwordForm.newPassword" type="password" minlength="6" required placeholder="至少6位字符" /></label>
          <label>确认新密码<input v-model="passwordForm.confirmPassword" type="password" minlength="6" required placeholder="再次输入新密码" /></label>
          <button class="primary-button" type="submit" :disabled="savingPassword">{{ savingPassword ? '修改中...' : '修改密码' }}</button>
        </form>
      </section>
      <section class="user-center-card favorites-card">
  <div class="card-title">
    <div>
      <span class="muted-label">FAVORITES</span>
      <h2>我的收藏</h2>
    </div>

    <span class="favorite-count">
      {{ favorites.length }} 件
    </span>
  </div>

  <p
    v-if="favoritesLoading"
    class="state-text"
  >
    收藏加载中...
  </p>

  <div
    v-else-if="favorites.length"
    class="favorite-list"
  >
    <article
      v-for="item in favorites"
      :key="item.id"
      class="favorite-item"
    >
      <img
        :src="item.imageUrl"
        :alt="item.productName"
      />

      <div class="favorite-info">
        <button
          type="button"
          class="favorite-name"
          @click="
            router.push(`/products/${item.productId}`)
          "
        >
          {{ item.productName }}
        </button>

        <p>
          {{ item.productDescription || '精选好物' }}
        </p>

        <strong>
          ￥{{ Number(item.productPrice || 0).toFixed(2) }}
        </strong>
      </div>

      <button
        class="delete-button"
        type="button"
        @click="removeFavorite(item.productId)"
      >
        取消收藏
      </button>
    </article>
  </div>

  <div
    v-else
    class="favorite-empty"
  >
    <p>还没有收藏商品</p>

    <button
      class="outline-button"
      type="button"
      @click="router.push('/products')"
    >
      去逛逛
    </button>
  </div>
</section>

    </div>
  </main>
</template>
