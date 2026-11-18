<script setup>
import { computed, onMounted, ref } from 'vue'//computed用于计算购物车总价。
import { useRouter } from 'vue-router'
import request from '../api/request'


const router = useRouter()

// 购物车列表
const carts = ref([])

// 页面加载状态
const loading = ref(false)

// 页面提示信息
const message = ref('')

// 提示信息类型
const messageType = ref('')

// 记录正在修改的购物车 ID
const updatingCartId = ref(null)

// 计算购物车商品总价
//遍历所有购物车记录，每条记录按照 
// 商品单价 × 商品数量 计算小计，最后把所有小计相加。
const totalPrice = computed(() => {
  return carts.value.reduce((total, cart) => {
    const price = Number(cart.productPrice || 0)
    const quantity = Number(cart.quantity || 0)

    return total + price * quantity
  }, 0)
})



// 获取购物车列表
async function loadCart() {
  loading.value = true
  message.value = ''

  try {
    const response = await request.get(
      '/cart'
    )

    if (response.data.code === 200) {
      carts.value = response.data.data
    } else {
      message.value = response.data.message || '获取购物车失败'
      messageType.value = 'error'
    }
  } catch (error) {
    console.error('获取购物车失败：', error)

    if (error.response && error.response.status === 401) {
      message.value = '请先登录后查看购物车'
    } else {
      message.value = '获取购物车失败，请检查后端是否启动'
    }

    messageType.value = 'error'
  } finally {
    loading.value = false
  }
}

// 修改购物车数量
async function updateQuantity(cart) {
  if (cart.quantity < 1) {
    cart.quantity = 1
    return
  }

  updatingCartId.value = cart.id
  message.value = ''

  try {
    const response = await request.put(
      `/cart/${cart.id}`,
      {
        quantity: cart.quantity
      },
      {
        withCredentials: true
      }
    )

    if (response.data.code === 200) {
      message.value = '数量修改成功'
      messageType.value = 'success'

      // 使用后端返回的最新数据更新当前购物车记录
      const updatedCart = response.data.data
      const index = carts.value.findIndex(
        item => item.id === updatedCart.id
      )

      if (index !== -1) {
        carts.value[index] = updatedCart
      }
    } else {
      message.value = response.data.message || '修改数量失败'
      messageType.value = 'error'
    }
  } catch (error) {
    console.error('修改购物车数量失败：', error)
    message.value = '修改数量失败，请检查库存或后端状态'
    messageType.value = 'error'
  } finally {
    updatingCartId.value = null
  }
}

// 删除购物车记录
async function deleteCart(cartId) {
  const confirmed = window.confirm('确定要删除这个购物车商品吗？')

  if (!confirmed) {
    return
  }

  try {
    const response = await request.delete(
      `/cart/${cartId}`
    )

    if (response.data.code === 200) {
      carts.value = carts.value.filter(
        cart => cart.id !== cartId
      )

      message.value = '删除成功'
      messageType.value = 'success'
    } else {
      message.value = response.data.message || '删除失败'
      messageType.value = 'error'
    }
  } catch (error) {
    console.error('删除购物车记录失败：', error)
    message.value = '删除失败，请检查后端是否启动'
    messageType.value = 'error'
  }
}

// 返回商品列表
function goToProducts() {
  router.push('/products')
}

onMounted(() => {
  loadCart()
})

// 去结算页面
function goToCheckout() {
  router.push('/checkout')
}

</script>

<template>
  <main class="cart-page">
    <header class="cart-header">
      <div>
        <h1>购物车</h1>
        <p>查看和管理已加入购物车的商品</p>
      </div>

      <button
        class="continue-button"
        type="button"
        @click="goToProducts"
      >
        继续购物
      </button>
    </header>

    <p
      v-if="message"
      class="cart-message"
      :class="messageType"
    >
      {{ message }}
    </p>

    <p
      v-if="loading"
      class="loading-message"
    >
      购物车加载中...
    </p>

    <section
  v-else-if="carts.length > 0"
  class="cart-list"
>
  <article
    v-for="cart in carts"
    :key="cart.id"
    class="cart-item"
  >
    <div class="cart-item-main">
      <h2>
        {{ cart.productName || `商品编号：${cart.productId}` }}
      </h2>

      <p class="cart-description">
        {{ cart.productDescription || '暂无商品描述' }}
      </p>

      <p class="cart-unit-price">
        单价：￥{{ Number(cart.productPrice || 0).toFixed(2) }}
      </p>

      <p class="cart-create-time">
        加入时间：{{ cart.createTime || '暂无' }}
      </p>
    </div>

    <div class="cart-item-control">
      <label>
        数量：

        <input
          v-model.number="cart.quantity"
          type="number"
          min="1"
          @change="updateQuantity(cart)"
        />
      </label>

      <span class="cart-subtotal">
        小计：￥{{
          (
            Number(cart.productPrice || 0) *
            Number(cart.quantity || 0)
          ).toFixed(2)
        }}
      </span>

      <button
        class="delete-button"
        type="button"
        @click="deleteCart(cart.id)"
      >
        删除
      </button>
    </div>

    <span
      v-if="updatingCartId === cart.id"
      class="updating-text"
    >
      修改中...
    </span>
  </article>

  <!-- 购物车总价必须放在商品列表内部 -->
  <section class="cart-summary">
    <span>购物车总价：</span>

    <div>
    <span>购物车总价：</span>

    <strong>
      ￥{{ totalPrice.toFixed(2) }}
    </strong>
  </div>

 
   <button
    class="checkout-button"
    type="button"
    @click="goToCheckout"
  >
    去结算
  </button>
  </section>
</section>

<!-- v-else 必须紧跟在上面的 v-else-if 后面 -->
<section
  v-else
  class="empty-cart"
>
  <p>购物车还是空的</p>

  <button
    class="primary-button"
    type="button"
    @click="goToProducts"
  >
    去挑选商品
  </button>
</section>
  </main>
</template>
