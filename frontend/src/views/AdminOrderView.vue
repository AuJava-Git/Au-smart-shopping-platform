<script setup>
import { onMounted, ref } from 'vue'
import request from '../api/request'
const orders = ref([]); const loading = ref(false); const savingId = ref(null); const message = ref(''); const messageType = ref(''); const backendReady = ref(true)
function getStatus(status) { const value = Number(status); if (value === 1) return '待支付'; if (value === 2) return '已支付'; if (value === 3) return '已取消'; return '未知状态' }
function showMessage(text, type = 'success') { message.value = text; messageType.value = type }
async function loadOrders() { loading.value = true; try { const response = await request.get('/admin/orders'); if (response.data.code === 200) { orders.value = response.data.data || []; backendReady.value = true } else showMessage(response.data.message || '获取订单失败', 'error') } catch (error) { backendReady.value = false; showMessage(error.response?.data?.message || '管理员订单接口暂不可用', 'error') } finally { loading.value = false } }
async function updateStatus(order, event) { const status = Number(event.target.value); if (status === Number(order.status)) return; savingId.value = order.id; try { const response = await request.put(`/admin/orders/${order.id}/status`, { status }); if (response.data.code === 200) { order.status = status; showMessage('订单状态修改成功') } else showMessage(response.data.message || '状态修改失败', 'error') } catch (error) { showMessage(error.response?.data?.message || '状态修改失败，请稍后重试', 'error') } finally { savingId.value = null } }
onMounted(loadOrders)
</script>

<template>
  <main class="admin-order-page page-shell">
    <header class="page-heading"><div><div class="section-kicker">ADMIN CONSOLE</div><h1>订单管理</h1><p>查看平台订单并处理订单状态</p></div><button class="secondary-button" type="button" @click="loadOrders">刷新订单</button></header>
    <div v-if="!backendReady" class="backend-placeholder"><h2>管理员订单接口暂不可用</h2><p>请确认后端已启动，并已登录管理员账号。</p></div>
    <p v-if="message" class="notice" :class="messageType">{{ message }}</p><p v-if="loading" class="state-text">订单加载中...</p>
    <section v-else-if="orders.length" class="admin-table-card"><div class="table-scroll"><table class="data-table"><thead><tr><th>订单号</th><th>用户 ID</th><th>金额</th><th>状态</th><th>创建时间</th><th>操作</th></tr></thead><tbody><tr v-for="order in orders" :key="order.id"><td>{{ order.orderNo }}</td><td>{{ order.userId }}</td><td class="price-text">￥{{ Number(order.totalAmount || 0).toFixed(2) }}</td><td><span class="order-status" :class="`status-${Number(order.status)}`">{{ getStatus(order.status) }}</span></td><td>{{ order.createTime || '暂无' }}</td><td><select class="status-select" :value="order.status" :disabled="savingId === order.id" @change="updateStatus(order, $event)"><option value="1">待支付</option><option value="2">已支付</option><option value="3">已取消</option></select></td></tr></tbody></table></div></section>
    <section v-else-if="backendReady" class="empty-state"><div class="empty-icon">◎</div><h2>暂无订单</h2><p>平台还没有创建过订单。</p></section>
  </main>
</template>
