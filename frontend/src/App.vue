<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import MainHeader from './components/MainHeader.vue'
import CustomerChatWidget from './components/CustomerChatWidget.vue'

const route = useRoute()
const showHeader = computed(() => route.meta.showHeader === true)
</script>

<template>
  <MainHeader v-if="showHeader" />
  <router-view />
  <CustomerChatWidget v-if="showHeader && !route.path.startsWith('/admin/')" />
</template>
