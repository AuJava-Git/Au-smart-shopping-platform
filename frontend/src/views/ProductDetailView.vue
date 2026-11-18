<script setup>
import { onMounted, ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../api/request'

const route = useRoute(); const router = useRouter(); const product = ref(null); const quantity = ref(1); const loading = ref(false); const adding = ref(false); const favoriteLoading = ref(false); const isFavorite = ref(false); const reviews = ref([]); const reviewsLoading = ref(false); const submittingReview = ref(false); const reviewPermission = ref({ loggedIn: false, purchased: false, canReview: false, message: '登录并购买该商品后才能评论' }); const reviewForm = ref({ rating: 5, content: '', anonymous: false }); const appendReviewId = ref(null); const appendContent = ref(''); const message = ref(''); const messageType = ref(''); const activeImage = ref(''); const productId = route.params.id
const fallbackImage = 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=900&q=80'
const averageRating = computed(() => { if (!reviews.value.length) return '暂无评分'; return (reviews.value.reduce((sum, item) => sum + Number(item.rating || 0), 0) / reviews.value.length).toFixed(1) })
function imageOf() { return product.value?.imageUrl || fallbackImage }
function hasStock() { return product.value && Number(product.value.stock || 0) > 0 }
function handleQuantityInput() { const stock = Number(product.value?.stock || 0); let value = Number(quantity.value); if (!Number.isInteger(value) || value < 1) value = 1; if (stock > 0 && value > stock) value = stock; quantity.value = value }
function increaseQuantity() { if (hasStock() && quantity.value < product.value.stock) quantity.value++ }
function decreaseQuantity() { if (quantity.value > 1) quantity.value-- }
function stars(rating) { return '★'.repeat(Number(rating || 0)) + '☆'.repeat(5 - Number(rating || 0)) }
function reviewUserName(item) { return item.anonymous ? '匿名用户' : (item.nickname || item.username || '实名用户') }
async function loadProductDetail() { loading.value = true; try { const response = await request.get(`/products/${productId}`); if (response.data.code === 200 && response.data.data) { product.value = response.data.data; activeImage.value = imageOf(); await Promise.all([loadFavoriteState(), loadReviews(), loadReviewPermission()]) } else { message.value = response.data.message || '商品不存在'; messageType.value = 'error' } } catch (error) { message.value = error.response?.data?.message || '获取商品详情失败，请检查后端'; messageType.value = 'error' } finally { loading.value = false } }
async function loadFavoriteState() { try { const response = await request.get(`/favorites/check/${productId}`); isFavorite.value = response.data.code === 200 && response.data.data === true } catch { isFavorite.value = false } }
async function loadReviews() {
  reviewsLoading.value = true

  try {
    const response = await request.get(
      `/reviews/product/${productId}`
    )

    console.log(
      '评论查询接口返回：',
      response.data
    )

    if (response.data.code !== 200) {
      throw new Error(
        response.data.message || '获取评论失败'
      )
    }

    reviews.value = response.data.data || []
  } catch (error) {
    console.error(
      '获取评论失败：',
      error
    )

    message.value =
      error.response?.data?.message ||
      error.message ||
      '获取评论失败，请检查后端控制台'

    messageType.value = 'error'
  } finally {
    reviewsLoading.value = false
  }
}

async function loadReviewPermission() { try { const response = await request.get(`/reviews/eligibility/${productId}`); if (response.data.code === 200 && response.data.data) reviewPermission.value = response.data.data } catch { reviewPermission.value = { loggedIn: false, purchased: false, canReview: false, message: '登录并购买该商品后才能评论' } } }
async function toggleFavorite() { if (favoriteLoading.value) return; favoriteLoading.value = true; try { const response = isFavorite.value ? await request.delete(`/favorites/${productId}`) : await request.post(`/favorites/${productId}`); if (response.data.code === 200) { isFavorite.value = !isFavorite.value; message.value = isFavorite.value ? '收藏成功' : '已取消收藏'; messageType.value = 'success' } else { message.value = response.data.message || '收藏操作失败'; messageType.value = 'error' } } catch (error) { message.value = error.response?.data?.message || '请先登录后再收藏'; messageType.value = 'error' } finally { favoriteLoading.value = false } }
async function addToCart() { if (!hasStock()) { message.value = '当前商品暂无库存'; messageType.value = 'error'; return }; handleQuantityInput(); adding.value = true; try { const response = await request.post('/cart', { productId: product.value.id, quantity: quantity.value }); message.value = response.data.code === 200 ? '加入购物车成功' : (response.data.message || '加入购物车失败'); messageType.value = response.data.code === 200 ? 'success' : 'error' } catch (error) { message.value = error.response?.data?.message || '请先登录后再加入购物车'; messageType.value = 'error' } finally { adding.value = false } }
async function submitReview() { if (!reviewPermission.value.canReview) { message.value = reviewPermission.value.message; messageType.value = 'error'; return }; if (!reviewForm.value.content.trim()) { message.value = '请输入评论内容'; messageType.value = 'error'; return }; submittingReview.value = true; try { const response = await request.post('/reviews', { productId: Number(productId), rating: reviewForm.value.rating, content: reviewForm.value.content.trim(), anonymous: reviewForm.value.anonymous }); if (response.data.code === 200) { reviewForm.value.content = ''; reviewForm.value.rating = 5; reviewForm.value.anonymous = false; message.value = '评论发布成功'; messageType.value = 'success'; await loadReviews() } else { message.value = response.data.message || '评论发布失败'; messageType.value = 'error' } } catch (error) { message.value = error.response?.data?.message || '评论发布失败，请检查后端日志'; messageType.value = 'error' } finally { submittingReview.value = false } }

async function appendReview(item) { if (!appendContent.value.trim()) { message.value = '请输入追加内容'; messageType.value = 'error'; return }; try { const response = await request.put(`/reviews/${item.id}/append`, { content: appendContent.value.trim() }); if (response.data.code === 200) { appendContent.value = ''; appendReviewId.value = null; message.value = '追加评论成功'; messageType.value = 'success'; await loadReviews() } else { message.value = response.data.message || '追加评论失败'; messageType.value = 'error' } } catch (error) { message.value = error.response?.data?.message || '追加评论失败'; messageType.value = 'error' } }
async function deleteReview(item) { if (!window.confirm('确定删除这条评论吗？')) return; try { const response = await request.delete(`/reviews/${item.id}`); if (response.data.code === 200) { message.value = '评论删除成功'; messageType.value = 'success'; await Promise.all([loadReviews(), loadReviewPermission()]) } else { message.value = response.data.message || '删除评论失败'; messageType.value = 'error' } } catch (error) { message.value = error.response?.data?.message || '删除评论失败'; messageType.value = 'error' } }
onMounted(loadProductDetail)
</script>

<template>
  <main class="product-detail-page"><button class="back-button" type="button" @click="router.push('/products')">← 返回商品列表</button><p v-if="loading" class="loading-message">商品详情加载中...</p>
    <section v-else-if="product" class="product-detail-card product-detail-layout"><div class="product-gallery"><div class="main-product-image"><img :src="activeImage || imageOf()" :alt="product.name" @error="$event.target.src = fallbackImage" /></div><div class="image-thumbs"><button class="image-thumb active" type="button" @click="activeImage = imageOf()"><img :src="imageOf()" :alt="product.name" @error="$event.target.src = fallbackImage" /></button></div></div>
      <div class="product-detail-info"><span class="product-category">{{ product.categoryName || '精选好物' }}</span><h1>{{ product.name }}</h1><p class="product-detail-description">{{ product.description || '暂无商品描述' }}</p><div class="detail-meta"><span>品质优选</span><span>库存充足</span><span>支持售后</span></div><p class="product-detail-price">￥{{ Number(product.price || 0).toFixed(2) }}</p><p class="product-detail-stock">当前库存：<span :class="hasStock() ? 'in-stock' : 'out-of-stock'">{{ hasStock() ? product.stock : '暂无库存' }}</span></p><div v-if="hasStock()" class="quantity-control"><span>购买数量：</span><button type="button" @click="decreaseQuantity">−</button><input v-model.number="quantity" type="number" min="1" :max="product.stock" @change="handleQuantityInput" /><button type="button" @click="increaseQuantity">＋</button></div><div class="product-detail-actions"><button class="add-cart-button" type="button" :disabled="!hasStock() || adding" @click="addToCart">{{ adding ? '加入中...' : hasStock() ? '加入购物车' : '暂无库存' }}</button><button class="favorite-button" :class="{ active: isFavorite }" type="button" :disabled="favoriteLoading" @click="toggleFavorite">{{ favoriteLoading ? '处理中...' : isFavorite ? '♥ 已收藏' : '♡ 收藏商品' }}</button><button class="go-cart-button" type="button" @click="router.push('/cart')">查看购物车</button></div><p v-if="message" class="product-message" :class="messageType">{{ message }}</p></div>
    </section><section v-else class="product-detail-error"><p>{{ message || '没有找到该商品' }}</p><button class="primary-button" type="button" @click="router.push('/products')">返回商品列表</button></section>
    <section v-if="product" class="detail-introduction"><div class="section-kicker">PRODUCT STORY</div><h2>商品介绍</h2><p>{{ product.description || '这是一件经过精选的优质商品，适合日常使用。' }}</p></section>
    <section v-if="product" class="review-section"><div class="review-heading"><div><div class="section-kicker">CUSTOMER REVIEWS</div><h2>用户评价</h2></div><div class="rating-summary"><strong>{{ averageRating }}</strong><span>★★★★★</span><small>{{ reviews.length }} 条评价</small></div></div><div v-if="reviewPermission.canReview" class="review-form"><form @submit.prevent="submitReview"><div class="rating-input"><span>我的评分：</span><button v-for="star in 5" :key="star" type="button" :class="{ selected: star <= reviewForm.rating }" @click="reviewForm.rating = star">★</button></div><textarea v-model="reviewForm.content" maxlength="500" placeholder="分享你对这个商品的使用感受"></textarea><label class="anonymous-option"><input v-model="reviewForm.anonymous" type="checkbox" /> 匿名评论 <small>其他用户将只看到“匿名用户”</small></label><div class="review-form-footer"><small>{{ reviewForm.content.length }}/500</small><button class="primary-button" type="submit" :disabled="submittingReview">{{ submittingReview ? '发布中...' : '发布评价' }}</button></div></form></div><div v-else class="review-permission-tip">{{ reviewPermission.message }}</div><p v-if="reviewsLoading" class="state-text">评价加载中...</p><div v-else-if="reviews.length" class="review-list"><article v-for="item in reviews" :key="item.id" class="review-item"><div class="review-item-header"><strong>{{ reviewUserName(item) }}</strong><span class="review-stars">{{ stars(item.rating) }}</span><time>{{ item.createTime?.replace('T', ' ') }}</time></div><p>{{ item.content }}</p><div v-if="item.mine" class="review-actions"><button type="button" class="secondary-button" @click="appendReviewId = appendReviewId === item.id ? null : item.id">追加评论</button><button type="button" class="danger-button" @click="deleteReview(item)">删除评论</button><div v-if="appendReviewId === item.id" class="append-review-box"><textarea v-model="appendContent" maxlength="500" placeholder="请输入追加内容"></textarea><button type="button" class="primary-button" @click="appendReview(item)">提交追加</button></div></div></article></div><div v-else class="review-empty">还没有评价。</div></section>
  </main>
</template>
