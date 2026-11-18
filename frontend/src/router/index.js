import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import ProductListView from '../views/ProductListView.vue'
import ProductDetailView from '../views/ProductDetailView.vue'
import CartView from '../views/CartView.vue'
import CheckoutView from '../views/CheckoutView.vue'
import OrderListView from '../views/OrderListView.vue'
import OrderDetailView from '../views/OrderDetailView.vue'
import UserCenterView from '../views/UserCenterView.vue'
import AdminProductView from '../views/AdminProductView.vue'
import AdminOrderView from '../views/AdminOrderView.vue'
import FeatureCenterView from '../views/FeatureCenterView.vue'
import AdminReviewView from '../views/AdminReviewView.vue'
import AdminCategoryView from '../views/AdminCategoryView.vue'
import ChatView from '../views/ChatView.vue'
import NotFoundView from '../views/NotFoundView.vue'
import request from '../api/request'

let currentUser = null

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/products' },
    { path: '/login', name: 'Login', component: LoginView },
    { path: '/register', name: 'Register', component: RegisterView },
    {
      path: '/products', name: 'Products', component: ProductListView,
      meta: { showHeader: true }
    },
    {
      path: '/products/:id', name: 'ProductDetail', component: ProductDetailView,
      meta: { showHeader: true }
    },
    {
      path: '/cart', name: 'Cart', component: CartView,
      meta: { showHeader: true, requiresAuth: true }
    },
    {
      path: '/checkout', name: 'Checkout', component: CheckoutView,
      meta: { showHeader: true, requiresAuth: true }
    },
    {
      path: '/orders', name: 'Orders', component: OrderListView,
      meta: { showHeader: true, requiresAuth: true }
    },
    {
      path: '/orders/:orderId', name: 'OrderDetail', component: OrderDetailView,
      meta: { showHeader: true, requiresAuth: true }
    },
    {
      path: '/user-center', name: 'UserCenter', component: UserCenterView,
      meta: { showHeader: true, requiresAuth: true }
    },
    {
      path: '/chat', name: 'Chat', component: ChatView,
      meta: { showHeader: true, requiresAuth: true }
    },
    { path: '/features', name: 'Features', component: FeatureCenterView, meta: { showHeader: true, requiresAuth: true } },
    {
      path: '/admin/products', name: 'AdminProducts', component: AdminProductView,
      meta: { showHeader: true, requiresAuth: true, requiresAdmin: true }
    },
    {
      path: '/admin/orders', name: 'AdminOrders', component: AdminOrderView,
      meta: { showHeader: true, requiresAuth: true, requiresAdmin: true }
    },
    { path: '/admin/reviews', name: 'AdminReviews', component: AdminReviewView, meta: { showHeader: true, requiresAuth: true, requiresAdmin: true } },
    { path: '/admin/categories', name: 'AdminCategories', component: AdminCategoryView, meta: { showHeader: true, requiresAuth: true, requiresAdmin: true } },
    { path: '/:pathMatch(.*)*', name: 'NotFound', component: NotFoundView }
  ]
})

router.beforeEach(async (to) => {
  if (!to.meta.requiresAuth) return true

  try {
    if (!currentUser) {
      const response = await request.get('/users/me')
      if (response.data.code !== 200 || !response.data.data) {
        return { path: '/login', query: { redirect: to.fullPath } }
      }
      currentUser = response.data.data
    }

    if (to.meta.requiresAdmin && Number(currentUser.role) !== 1) {
      return { path: '/products' }
    }

    return true
  } catch (error) {
    console.error('检查登录状态失败：', error)
    currentUser = null
    return { path: '/login', query: { redirect: to.fullPath } }
  }
})

export function setCurrentUser(user) {
  currentUser = user || null
}

export function clearCurrentUser() {
  currentUser = null
}

export default router
