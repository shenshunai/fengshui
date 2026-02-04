import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue')
  },
  {
    path: '/zodiac',
    name: 'Zodiac',
    component: () => import('@/views/Zodiac.vue')
  },
  {
    path: '/bazi',
    name: 'Bazi',
    component: () => import('@/views/Bazi.vue')
  },
  {
    path: '/divine',
    name: 'Divine',
    component: () => import('@/views/Divine.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
