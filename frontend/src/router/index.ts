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
  },
  {
    path: '/fengshui',
    name: 'Fengshui',
    component: () => import('@/views/Fengshui.vue')
  },
  {
    path: '/calendar',
    name: 'Calendar',
    component: () => import('@/views/Calendar.vue')
  },
  {
    path: '/naming',
    name: 'Naming',
    component: () => import('@/views/Naming.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
