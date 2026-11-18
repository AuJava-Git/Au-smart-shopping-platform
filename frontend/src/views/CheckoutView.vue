<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../api/request'

const router = useRouter()
const loading = ref(false)
const message = ref('')
const messageType = ref('')
const addresses = ref([])
const addressId = ref(null)
async function loadAddresses() { const response = await request.get('/addresses'); addresses.value = response.data.data || []; addressId.value = addresses.value.find(item => item.isDefault)?.id || addresses.value[0]?.id || null }

async function submitOrder() {
  if (loading.value) return
  loading.value = true
  message.value = ''

  try {
    const response = await request.post('/orders', { addressId: addressId.value })

    if (response.data.code === 200) {
      message.value = '订单创建成功，正在进入我的订单'
      messageType.value = 'success'
      window.setTimeout(() => router.push('/orders'), 700)
    } else {
      message.value = response.data.message || '订单创建失败'
      messageType.value = 'error'
    }
  } catch (error) {
    console.error('创建订单失败：', error)
    message.value = error.response?.data?.message || '订单创建失败，请检查后端'
    messageType.value = 'error'
  } finally {
    loading.value = false
  }
}

function goBack() {
  router.push('/cart')
}
onMounted(loadAddresses)
</script>

<template>
  <main class="checkout-page">
    <section class="checkout-card">
      <div class="section-kicker">ORDER CONFIRMATION</div>
      <h1>确认订单</h1>
      <p>当前将提交购物车中的全部商品。</p>
      <label class="checkout-address">收货地址<select v-model="addressId" required><option :value="null" disabled>请选择收货地址</option><option v-for="item in addresses" :key="item.id" :value="item.id">{{ item.receiverName }} {{ item.receiverPhone }} - {{ item.province }}{{ item.city }}{{ item.district }}{{ item.detailAddress }}</option></select></label>
      <p class="checkout-warning">提交订单后，购物车中的商品会被清空。</p>

      <button class="submit-order-button" type="button" :disabled="loading" @click="submitOrder">
        {{ loading ? '提交中...' : '确认提交订单' }}
      </button>
      <button class="back-cart-button" type="button" @click="goBack">返回购物车</button>

      <p v-if="message" class="message" :class="messageType">{{ message }}</p>
    </section>
  </main>
</template>
