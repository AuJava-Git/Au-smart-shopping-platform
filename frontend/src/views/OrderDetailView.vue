<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../api/request'

const route = useRoute()
const router = useRouter()

const order = ref(null)
const items = ref([])
const loading = ref(false)
const message = ref('')

const orderId = route.params.orderId

async function loadOrderDetail() {
  loading.value = true
  message.value = ''

  try {
    const response = await request.get(
      `/orders/${orderId}`
    )

    console.log('订单详情接口完整返回：', response.data)

    if (response.data.code === 200) {
      const detail = response.data.data

      if (!detail || !detail.order) {
        message.value = '后端返回的订单详情数据为空'
        return
      }

      order.value = detail.order
      items.value = detail.items || []
    } else {
      message.value =
        response.data.message || '获取订单详情失败'
    }
  } catch (error) {
    console.error('获取订单详情失败：', error)

    if (error.response) {
      console.error('HTTP 状态码：', error.response.status)
      console.error('后端返回内容：', error.response.data)

      if (
        error.response.data &&
        error.response.data.message
      ) {
        message.value = error.response.data.message
      } else {
        message.value =
          `获取订单详情失败，HTTP 状态码：${error.response.status}`
      }
    } else if (error.request) {
      message.value =
        '请求已发送，但后端没有返回结果'
    } else {
      message.value = '订单详情请求配置错误'
    }
  } finally {
    loading.value = false
  }
}


function getOrderStatus(status) {
  if (status === 1) {
    return '待支付'
  }

  if (status === 2) {
    return '已支付'
  }

  if (status === 3) {
    return '已取消'
  }

  return '未知状态'
}

function goBack() {
  router.push('/orders')
}

onMounted(() => {
  loadOrderDetail()
})
</script>

<template>
  <main class="order-detail-page">
    <button
      class="back-button"
      type="button"
      @click="goBack"
    >
      ← 返回我的订单
    </button>

    <p
      v-if="loading"
      class="loading-message"
    >
      订单详情加载中...
    </p>

    <p
      v-else-if="message"
      class="error-message"
    >
      {{ message }}
    </p>

    <section
      v-else-if="order"
      class="order-detail-card"
    >
      <header class="order-detail-header">
        <div>
          <h1>订单详情</h1>
          <p>订单号：{{ order.orderNo }}</p>
          <p>创建时间：{{ order.createTime }}</p>
        </div>

        <span class="order-detail-status">
          {{ getOrderStatus(order.status) }}
        </span>
      </header>

      <section class="order-item-list">
        <article
          v-for="item in items"
          :key="item.id"
          class="order-item"
        >
          <div>
            <h2>{{ item.productName }}</h2>
            <p>商品编号：{{ item.productId }}</p>
          </div>

          <div class="order-item-info">
            <span>
              单价：￥{{ Number(item.productPrice).toFixed(2) }}
            </span>

            <span>
              数量：{{ item.quantity }}
            </span>

            <strong>
              小计：￥{{ Number(item.subtotal).toFixed(2) }}
            </strong>
          </div>
        </article>
      </section>

      <footer class="order-detail-footer">
        <span>订单总金额：</span>

        <strong>
          ￥{{ Number(order.totalAmount).toFixed(2) }}
        </strong>
      </footer>
    </section>
    <p
  v-else
    class="empty-message"
    >
    没有找到订单详情
    </p>
  </main>
</template>
