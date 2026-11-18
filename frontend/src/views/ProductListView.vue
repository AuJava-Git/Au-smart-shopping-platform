<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../api/request'

const router = useRouter()
const products = ref([])
const keyword = ref('')
const currentPage = ref(1)
const pageSize = ref(8)
const totalPages = ref(1)
const loading = ref(false)
const message = ref('')
const messageType = ref('')
const searching = ref(false)
const categoryId = ref(null)
const categories = ref([{ id: null, name: '全部商品' }])
const fallbackImage = 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80'
function imageOf(product) { return product.imageUrl || fallbackImage }
function hasStock(product) { return Number(product.stock || 0) > 0 }
async function loadCategories() { try { const response = await request.get('/categories'); if (response.data.code === 200) categories.value = [{ id: null, name: '全部商品' }, ...(response.data.data || [])] } catch (error) { console.error('分类加载失败', error) } }
async function loadProducts() {
  loading.value = true
  message.value = ''
  messageType.value = ''

  try {
    if (searching.value && keyword.value.trim()) {
      const response = await request.get('/products/search', {
        params: {
          name: keyword.value.trim()
        }
      })

      if (response.data.code !== 200) {
        throw new Error(response.data.message || '商品搜索失败')
      }

      products.value = response.data.data || []
      totalPages.value = 1
      return
    }

    const response = await request.get('/products/page', {
      params: {
        page: currentPage.value,
        size: pageSize.value,
        categoryId: categoryId.value || undefined
      }
    })

    console.log('商品分页接口返回：', response.data)

    if (response.data.code !== 200) {
      throw new Error(response.data.message || '获取商品失败')
    }

    const page = response.data.data || {}

    products.value = page.records || []

    totalPages.value =
      page.pages ||
      Math.max(
        1,
        Math.ceil((page.total || 0) / pageSize.value)
      )
  } catch (error) {
    console.error('商品列表加载失败：', error)

    message.value =
      error.response?.data?.message ||
      error.message ||
      '获取商品失败，请检查后端控制台'

    messageType.value = 'error'
    products.value = []
  } finally {
    loading.value = false
  }
}

function searchProducts() { if (!keyword.value.trim()) return resetSearch(); searching.value = true; currentPage.value = 1; loadProducts() }
function resetSearch() { keyword.value = ''; searching.value = false; currentPage.value = 1; loadProducts() }
function selectCategory(id) { categoryId.value = id; searching.value = false; currentPage.value = 1; loadProducts() }
function goPage(step) { const next = currentPage.value + step; if (next < 1 || next > totalPages.value) return; currentPage.value = next; loadProducts() }
function scrollToCatalog() { document.querySelector('.product-catalog')?.scrollIntoView({ behavior: 'smooth' }) }
onMounted(async () => { await loadCategories(); await loadProducts() })
</script>

<template>
  <main class="product-page">
    <section class="product-hero"><div class="hero-content"><span class="section-kicker hero-kicker">SMART SHOPPING</span><h1>发现让生活更好的商品</h1><p>精选数码、家居、运动与生活好物，简单挑选，放心购买。</p><button class="hero-button" type="button" @click="scrollToCatalog">开始逛逛</button></div></section>
    <header class="product-header product-catalog"><div><div class="section-kicker">PRODUCT CATALOG</div><h1>商品列表</h1><p>从精选商品中找到适合你的好物</p></div></header>
    <section class="product-search"><input v-model="keyword" type="text" placeholder="搜索商品名称" @keyup.enter="searchProducts" /><button class="search-button" type="button" @click="searchProducts">搜索</button><button class="reset-button" type="button" @click="resetSearch">重置</button></section>
    <nav class="category-tabs"><button v-for="category in categories" :key="category.id || 'all'" type="button" :class="{ active: categoryId === category.id && !searching }" @click="selectCategory(category.id)">{{ category.name }}</button></nav>
    <p v-if="message" class="product-message" :class="messageType">{{ message }}</p><p v-if="loading" class="loading-message">商品加载中...</p>
    <section v-else-if="products.length" class="product-list"><article v-for="product in products" :key="product.id" class="product-card"><button class="product-image-button" type="button" @click="router.push(`/products/${product.id}`)"><img :src="imageOf(product)" :alt="product.name" @error="$event.target.src = fallbackImage" /></button><div class="product-card-content"><span v-if="product.categoryName" class="product-category">{{ product.categoryName }}</span><h2>{{ product.name }}</h2><p class="product-description">{{ product.description || '精选商品，品质生活。' }}</p><p class="product-price">￥{{ Number(product.price || 0).toFixed(2) }}</p><p class="product-stock">库存：<span :class="hasStock(product) ? 'in-stock' : 'out-of-stock'">{{ hasStock(product) ? product.stock : '暂无库存' }}</span></p></div><button class="view-product-button" type="button" @click="router.push(`/products/${product.id}`)">查看详情</button></article></section><section v-else class="empty-product"><p>{{ searching ? '没有找到相关商品' : '当前分类暂无商品' }}</p></section>
    <section v-if="!loading && products.length && !searching" class="pagination"><button type="button" :disabled="currentPage <= 1" @click="goPage(-1)">上一页</button><span>第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</span><button type="button" :disabled="currentPage >= totalPages" @click="goPage(1)">下一页</button></section>
  </main>
</template>
