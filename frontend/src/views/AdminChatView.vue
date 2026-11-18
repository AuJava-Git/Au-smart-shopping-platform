<script setup>
import { onBeforeUnmount, onMounted, ref } from 'vue'
import request from '../api/request'

const socket = ref(null)
const connected = ref(false)
const notices = ref([])
const conversations = ref({})
const selectedUserId = ref('')
const input = ref('')
const currentUser = ref(null)

function addConversationMessage(userId, sender, content, type = 'user') {
  if (!conversations.value[userId]) conversations.value[userId] = []
  conversations.value[userId].push({ sender, content, type, time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) })
}

async function loadAdmin() {
  const response = await request.get('/users/me')
  currentUser.value = response.data.data
  if (Number(currentUser.value?.role) !== 1) return
  connect()
}

function connect() {
  const protocol = window.location.protocol === 'https:' ? 'wss' : 'ws'
  socket.value = new WebSocket(`${protocol}://localhost:9000/ws/chat`)
  socket.value.onopen = () => {
    connected.value = true
    socket.value.send(JSON.stringify({ type: 'hello', role: 'admin', userId: String(currentUser.value.id), nickname: currentUser.value.nickname || '管理员' }))
  }
  socket.value.onmessage = event => {
    const data = JSON.parse(event.data)
    if (data.type === 'transfer_notice') {
      notices.value.unshift(data)
      selectedUserId.value = data.userId
      addConversationMessage(data.userId, data.nickname, data.content, 'notice')
    } else if (data.type === 'user_message') {
      addConversationMessage(data.userId, data.nickname, data.content, 'user')
      selectedUserId.value = data.userId
    }
  }
  socket.value.onclose = () => { connected.value = false }
}

function sendMessage() {
  const content = input.value.trim()
  if (!content || !selectedUserId.value || !socket.value || socket.value.readyState !== WebSocket.OPEN) return
  socket.value.send(JSON.stringify({ type: 'admin_message', userId: selectedUserId.value, content }))
  addConversationMessage(selectedUserId.value, '我', content, 'admin')
  input.value = ''
}

onMounted(() => { loadAdmin().catch(() => {}) })
onBeforeUnmount(() => { if (socket.value) socket.value.close() })
</script>

<template>
  <main class="admin-chat-page page-shell">
    <header class="page-heading"><div><div class="section-kicker">CUSTOMER SERVICE</div><h1>人工客服</h1><p>管理员打开此页面时，Au 会暂停自动回复；关闭页面后，Au 自动恢复。</p></div><span class="chat-connection-status" :class="{ online: connected }">{{ connected ? '客服在线' : '连接中' }}</span></header>
    <section class="admin-chat-layout">
      <aside class="admin-chat-users">
        <h2>转人工提醒</h2>
        <button v-for="notice in notices" :key="`${notice.userId}-${notice.time}`" type="button" class="admin-chat-user" :class="{ active: selectedUserId === notice.userId }" @click="selectedUserId = notice.userId">
          <span class="au-avatar small">{{ (notice.nickname || '用户').slice(0, 1) }}</span><span><strong>{{ notice.nickname }}</strong><small>申请人工客服</small></span>
        </button>
        <p v-if="!notices.length" class="chat-empty">暂无转人工用户</p>
      </aside>
      <section class="admin-chat-dialog">
        <template v-if="selectedUserId">
          <div class="admin-dialog-title">正在服务用户：{{ selectedUserId }}</div>
          <div class="admin-dialog-messages"><article v-for="(item, index) in conversations[selectedUserId] || []" :key="index" class="chat-message" :class="item.type"><div class="chat-message-name">{{ item.sender }}</div><div class="chat-bubble">{{ item.content }}</div></article></div>
          <div class="admin-chat-input"><input v-model="input" placeholder="回复用户" @keyup.enter="sendMessage" /><button type="button" @click="sendMessage">发送</button></div>
        </template>
        <div v-else class="chat-empty">等待用户发送消息或申请转人工</div>
      </section>
    </section>
  </main>
</template>
