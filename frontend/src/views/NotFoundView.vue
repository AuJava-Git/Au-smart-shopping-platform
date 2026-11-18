//增加 404 页面
<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

function goToProducts() {
  router.push('/products')
}
</script>

<template>
  <main class="not-found-page">
    <section class="not-found-card">
      <h1>404</h1>

      <h2>页面不存在</h2>

      <p>
        你访问的页面可能已经被删除，或者地址输入错误。
      </p>

      <button
        class="primary-button"
        type="button"
        @click="goToProducts"
      >
        返回商品列表
      </button>
    </section>
  </main>
</template>
