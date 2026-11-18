<script setup>
import { nextTick, onBeforeUnmount, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../api/request'

const router = useRouter()

const open = ref(false)
const connected = ref(false)
const adminOnline = ref(false)
const transferRequested = ref(false)
const input = ref('')
const messages = ref([])
const currentUser = ref(null)
const socket = ref(null)
const messagesBox = ref(null)

function addMessage(sender, content, type = 'ai') {
  messages.value.push({
    sender,
    content,
    type,
    time: new Date().toLocaleTimeString([], {
      hour: '2-digit',
      minute: '2-digit'
    })
  })

  nextTick(() => {
    if (messagesBox.value) {
      messagesBox.value.scrollTop = messagesBox.value.scrollHeight
    }
  })
}

async function loadUser() {
  try {
    const response = await request.get('/users/me')

    if (
      response.data.code === 200 &&
      response.data.data
    ) {
      currentUser.value = response.data.data
      connect()
    }
  } catch {
    currentUser.value = null
  }
}

function connect() {
  if (
    socket.value &&
    socket.value.readyState <= 1
  ) {
    return
  }

  const protocol =
    window.location.protocol === 'https:'
      ? 'wss'
      : 'ws'

  socket.value = new WebSocket(
    `${protocol}://localhost:9000/ws/chat`
  )

  socket.value.onopen = () => {
    connected.value = true

    socket.value.send(
      JSON.stringify({
        type: 'hello',
        role: 'user',
        userId: String(currentUser.value.id),
        nickname:
          currentUser.value.nickname ||
          currentUser.value.username
      })
    )
  }

  socket.value.onmessage = event => {
    const data = JSON.parse(event.data)

    if (data.type === 'ready') {
      adminOnline.value = Boolean(data.adminOnline)

      if (!messages.value.length) {
        addMessage(
          'Au',
          data.message ||
            '你好，我是 Au。有什么可以帮助你？'
        )
      }
    }

    else if (data.type === 'admin_status') {
      adminOnline.value = Boolean(data.online)

      addMessage(
        '系统',
        adminOnline.value
          ? '管理员客服已上线，接下来由人工客服协助你。'
          : '管理员已离开，Au 将继续为你服务。',
        'system'
      )
    }

    else if (data.type === 'ai_message') {
      addMessage(
        'Au',
        data.content,
        'ai'
      )
    }

    else if (data.type === 'human_message') {
      addMessage(
        data.sender || '管理员',
        data.content,
        'human'
      )
    }

    else if (
      data.type === 'human_waiting' ||
      data.type === 'transfer_result'
    ) {
      addMessage(
        '系统',
        data.message,
        'system'
      )
    }
  }

  socket.value.onclose = () => {
    connected.value = false
  }

  socket.value.onerror = () => {
    connected.value = false
  }
}

function toggleOpen() {
  open.value = !open.value

  if (
    open.value &&
    !currentUser.value &&
    !messages.value.length
  ) {
    addMessage(
      '系统',
      '请先登录后使用客服功能。',
      'system'
    )
  }
}

function sendMessage() {
  const content = input.value.trim()

  if (
    !content ||
    !socket.value ||
    socket.value.readyState !== WebSocket.OPEN
  ) {
    return
  }

  socket.value.send(
    JSON.stringify({
      type: 'message',
      content
    })
  )

  addMessage(
    '我',
    content,
    'user'
  )

  input.value = ''
}

function transfer() {
  if (
    !socket.value ||
    socket.value.readyState !== WebSocket.OPEN ||
    transferRequested.value
  ) {
    return
  }

  socket.value.send(
    JSON.stringify({
      type: 'transfer'
    })
  )

  transferRequested.value = true
}

function goLogin() {
  open.value = false

  router.push({
    path: '/login',
    query: {
      redirect:
        router.currentRoute.value.fullPath
    }
  })
}

onMounted(() => {
  loadUser()
})

onBeforeUnmount(() => {
  if (socket.value) {
    socket.value.close()
  }
})
</script>

<template>
  <div class="customer-chat-widget">
    <section
      v-if="open"
      class="customer-chat-panel"
    >
      <header class="customer-chat-header">
        <div class="au-avatar">
          AU
        </div>

        <div>
          <strong>Au 智能客服</strong>

          <small>
            {{
              adminOnline
                ? '人工客服在线'
                : 'Au 在线为你服务'
            }}
          </small>
        </div>

        <button
          type="button"
          class="chat-close"
          @click="open = false"
        >
          ×
        </button>
      </header>

      <div
        ref="messagesBox"
        class="customer-chat-messages"
      >
        <div
          v-if="!currentUser"
          class="chat-login-tip"
        >
          登录后即可咨询客服和转人工。
        </div>

        <article
          v-for="(item, index) in messages"
          :key="index"
          class="chat-message"
          :class="item.type"
        >
          <div class="chat-message-name">
            {{ item.sender }}
          </div>

          <div class="chat-bubble">
            {{ item.content }}
          </div>
        </article>
      </div>

      <footer class="customer-chat-footer">
        <button
          v-if="!currentUser"
          type="button"
          class="chat-login-button"
          @click="goLogin"
        >
          去登录
        </button>

        <template v-else>
          <button
            type="button"
            class="transfer-button"
            :disabled="
              transferRequested || !connected
            "
            @click="transfer"
          >
            {{
              transferRequested
                ? '已通知人工'
                : '转人工'
            }}
          </button>

          <div class="chat-input-row">
            <input
              v-model="input"
              placeholder="请输入你的问题"
              @keyup.enter="sendMessage"
            />

            <button
              type="button"
              @click="sendMessage"
            >
              发送
            </button>
          </div>
        </template>
      </footer>
    </section>

    <button
      type="button"
      class="customer-chat-trigger"
      @click="toggleOpen"
    >
      <span class="au-avatar">
        AU
      </span>

      <span>客服</span>
    </button>
  </div>
</template>
