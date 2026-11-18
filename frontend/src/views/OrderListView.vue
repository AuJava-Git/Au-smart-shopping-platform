<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../api/request'

const router = useRouter(); const orders = ref([]); const loading = ref(false); const actionId = ref(null); const message = ref(''); const messageType = ref('')
function getOrderStatus(status) { const value = Number(status); if (value === 1) return '待支付'; if (value === 2) return '已支付'; if (value === 3) return '已取消'; return '未知状态' }
function statusClass(status) { return `status-${Number(status)}` }
function showMessage(text, type = 'success') { message.value = text; messageType.value = type }
async function loadOrders() { loading.value = true; message.value = ''; try { const response = await request.get('/orders'); if (response.data.code === 200) orders.value = response.data.data || []; else showMessage(response.data.message || '获取订单列表失败', 'error') } catch (error) { showMessage(error.response?.data?.message || '获取订单列表失败，请检查后端', 'error') } finally { loading.value = false } }
async function orderAction(order, action) { actionId.value = order.id; try { const response = await request.put(`/orders/${order.id}/${action}`); if (response.data.code === 200) { showMessage(action === 'pay' ? '支付成功' : '订单已取消'); await loadOrders() } else showMessage(response.data.message || '操作失败', 'error') } catch (error) { showMessage(error.response?.data?.message || '操作失败，请稍后重试', 'error') } finally { actionId.value = null } }
onMounted(loadOrders)
</script>

<template>
  <main class="order-page page-shell">
    <header class="page-heading"><div><div class="section-kicker">ORDER CENTER</div><h1>我的订单</h1><p>查看和管理你创建过的订单</p></div><div class="heading-actions"><button class="secondary-button" type="button" @click="loadOrders">刷新订单</button><button class="primary-button" type="button" @click="router.push('/products')">继续购物</button></div></header>
    <p v-if="message" class="notice" :class="messageType">{{ message }}</p><p v-if="loading" class="state-text">订单加载中...</p>
    <section v-else-if="orders.length" class="order-list">
      <article v-for="order in orders" :key="order.id" class="order-card">
        <div class="order-main-info"><span class="muted-label">订单编号</span><h2>{{ order.orderNo }}</h2><p>创建时间：{{ order.createTime || '暂无' }}</p></div>
        <div class="order-side-info"><span class="order-status" :class="statusClass(order.status)">{{ getOrderStatus(order.status) }}</span><strong class="order-amount">￥{{ Number(order.totalAmount || 0).toFixed(2) }}</strong><div class="order-actions"><button class="outline-button" type="button" @click="router.push(`/orders/${order.id}`)">查看详情</button><button v-if="Number(order.status) === 1" class="pay-button" type="button" :disabled="actionId === order.id" @click="orderAction(order, 'pay')">{{ actionId === order.id ? '处理中...' : '立即支付' }}</button><button v-if="Number(order.status) === 1" class="cancel-button" type="button" :disabled="actionId === order.id" @click="orderAction(order, 'cancel')">取消订单</button></div></div>
      </article>
    </section>
    <section v-else class="empty-state"><div class="empty-icon">◎</div><h2>暂时没有订单</h2><p>去挑选喜欢的商品，提交订单后会显示在这里。</p><button class="primary-button" type="button" @click="router.push('/products')">去购买商品</button></section>
  </main>
</template>
