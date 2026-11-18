<script setup>
import { computed, nextTick, onBeforeUnmount, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const messages = ref([])
const draft = ref('')
const connected = ref(false)
const connecting = ref(false)
const sending = ref(false)
const reconnecting = ref(false)
const errorMessage = ref('')
const messageList = ref(null)

let socket = null
let reconnectTimer = null
let reconnectAttempts = 0
let manuallyClosed = false

const suggestions = ['帮我找耳机', '推荐一款运动鞋', '有没有办公椅', '帮我看看面霜']
const statusText = computed(() => {
  if (connected.value) return '已连接'
  if (connecting.value || reconnecting.value) return '连接中...'
  return '未连接'
})

function wsUrl() {
  // 前端开发服务器是 5173，WebSocket 实际由 Gateway 的 9000 端口提供。
  // 生产环境可以通过 VITE_GATEWAY_ORIGIN 覆盖，例如 https://api.example.com。
  const configuredOrigin = import.meta.env.VITE_GATEWAY_ORIGIN || 'http://localhost:9000'
  const url = new URL(configuredOrigin)
  url.protocol = url.protocol === 'https:' ? 'wss:' : 'ws:'
  url.pathname = '/ws/chat'
  url.search = ''
  url.hash = ''
  return url.toString()
}

function addMessage(role, payload) {
  messages.value.push({
    id: `${Date.now()}-${Math.random()}`,
    role,
    type: payload.type || 'text',
    text: payload.message || '',
    keyword: payload.keyword || '',
    products: Array.isArray(payload.products) ? payload.products : [],
    time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
  })
  scrollToBottom()
}

function connect() {
  if (manuallyClosed || socket?.readyState === WebSocket.OPEN || connecting.value) return
  connecting.value = true
  errorMessage.value = ''
  socket = new WebSocket(wsUrl())

  socket.onopen = () => {
    connected.value = true
    connecting.value = false
    reconnecting.value = false
    reconnectAttempts = 0
    errorMessage.value = ''
  }

  socket.onmessage = event => {
    try {
      const payload = JSON.parse(event.data)
      addMessage('assistant', payload)
    } catch {
      addMessage('assistant', { type: 'text', message: event.data })
    }
  }

  socket.onerror = () => {
    errorMessage.value = '聊天服务暂时不可用，请确认 Gateway 和聊天服务已启动。'
  }

  socket.onclose = () => {
    connected.value = false
    connecting.value = false
    socket = null
    if (!manuallyClosed) scheduleReconnect()
  }
}

function scheduleReconnect() {
  if (reconnectTimer || reconnectAttempts >= 5) {
    if (reconnectAttempts >= 5) {
      reconnecting.value = false
      errorMessage.value = '多次连接失败，请点击“重新连接”再试。'
    }
    return
  }
  reconnecting.value = true
  reconnectAttempts += 1
  const delay = Math.min(1000 * 2 ** (reconnectAttempts - 1), 8000)
  reconnectTimer = window.setTimeout(() => {
    reconnectTimer = null
    connect()
  }, delay)
}

function reconnectNow() {
  clearReconnectTimer()
  reconnectAttempts = 0
  errorMessage.value = ''
  if (socket && socket.readyState !== WebSocket.CLOSED) socket.close()
  connect()
}

function clearReconnectTimer() {
  if (reconnectTimer) {
    window.clearTimeout(reconnectTimer)
    reconnectTimer = null
  }
}

function sendMessage(text = draft.value) {
  const content = text.trim()
  if (!content || sending.value) return
  if (!connected.value || !socket) {
    errorMessage.value = '聊天服务尚未连接，请稍候或点击重新连接。'
    return
  }

  addMessage('user', { type: 'text', message: content })
  sending.value = true
  socket.send(JSON.stringify({ message: content }))
  draft.value = ''
  window.setTimeout(() => { sending.value = false }, 250)
}

function scrollToBottom() {
  nextTick(() => {
    if (messageList.value) messageList.value.scrollTop = messageList.value.scrollHeight
  })
}

function imageOf(product) {
  return product.imageUrl || 'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?auto=format&fit=crop&w=900&q=80'
}

function openProduct(product) {
  if (product?.id) router.push(`/products/${product.id}`)
}

function clearMessages() {
  messages.value = []
}

onMounted(() => {
  manuallyClosed = false
  messages.value.push({
    id: 'welcome',
    role: 'assistant',
    type: 'text',
    text: '你好，我是智能购物助手。告诉我你想找的商品，我会为你查询并展示商品卡片。',
    products: [],
    time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
  })
  connect()
})

onBeforeUnmount(() => {
  manuallyClosed = true
  clearReconnectTimer()
  if (socket) socket.close()
})
</script>

<template>
  <main class="chat-page">
    <section class="chat-shell">
      <header class="chat-header">
        <div>
          <div class="section-kicker">SMART SHOPPING ASSISTANT</div>
          <h1>智能客服</h1>
          <p>实时查询商品，快速找到适合你的好物</p>
        </div>
        <div class="chat-header-actions">
          <span class="connection-status" :class="{ online: connected }">
            <i></i>{{ statusText }}
          </span>
          <button class="chat-clear-button" type="button" @click="clearMessages">清空记录</button>
          <button class="chat-reconnect-button" type="button" @click="reconnectNow">重新连接</button>
        </div>
      </header>

      <div ref="messageList" class="chat-messages" aria-live="polite">
        <article
          v-for="item in messages"
          :key="item.id"
          class="chat-message-row"
          :class="item.role"
        >
          <div class="chat-avatar">{{ item.role === 'user' ? '我' : 'AI' }}</div>
          <div class="chat-message-content">
            <div v-if="item.text" class="chat-bubble">{{ item.text }}</div>

            <div v-if="item.type === 'product_card' && item.products.length" class="product-card-result">
              <div class="product-card-result-title">
                <strong>商品推荐</strong>
                <span v-if="item.keyword">关键词：{{ item.keyword }}</span>
              </div>
              <div class="chat-product-grid">
                <article v-for="product in item.products" :key="product.id" class="chat-product-card">
                  <button class="chat-product-image" type="button" @click="openProduct(product)">
                    <img :src="imageOf(product)" :alt="product.name" @error="$event.target.src = imageOf({})" />
                  </button>
                  <div class="chat-product-body">
                    <h3>{{ product.name }}</h3>
                    <p class="chat-product-description">{{ product.description || '精选商品，品质生活。' }}</p>
                    <div class="chat-product-footer">
                      <strong>￥{{ Number(product.price || 0).toFixed(2) }}</strong>
                      <span :class="Number(product.stock) > 0 ? 'stock-ok' : 'stock-empty'">
                        {{ Number(product.stock) > 0 ? `库存 ${product.stock}` : '暂时缺货' }}
                      </span>
                    </div>
                    <button class="chat-view-product" type="button" @click="openProduct(product)">查看详情</button>
                  </div>
                </article>
              </div>
            </div>
            <time>{{ item.time }}</time>
          </div>
        </article>
      </div>

      <div class="chat-suggestions">
        <span>试试这样问：</span>
        <button v-for="suggestion in suggestions" :key="suggestion" type="button" @click="sendMessage(suggestion)">
          {{ suggestion }}
        </button>
      </div>

      <p v-if="errorMessage" class="chat-error">{{ errorMessage }}</p>
      <form class="chat-composer" @submit.prevent="sendMessage()">
        <textarea
          v-model="draft"
          rows="1"
          maxlength="300"
          placeholder="输入商品关键词，例如：帮我找一款蓝牙耳机"
          @keydown.enter.exact.prevent="sendMessage()"
        ></textarea>
        <button type="submit" :disabled="!connected || !draft.trim() || sending">
          {{ sending ? '发送中' : '发送' }}
        </button>
      </form>
      <p class="chat-hint">按 Enter 发送，Shift + Enter 换行。商品卡片可以点击进入详情页。</p>
    </section>
  </main>
</template>
