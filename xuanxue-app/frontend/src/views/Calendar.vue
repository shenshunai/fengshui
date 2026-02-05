<template>
  <div class="calendar-page">
    <h1 class="page-title">万年历</h1>
    <p class="subtitle">查看指定日期的今日宜忌（由 AI 生成）</p>

    <div class="date-card">
      <el-date-picker
        v-model="selectedDate"
        type="date"
        placeholder="选择日期"
        format="YYYY-MM-DD"
        value-format="YYYY-MM-DD"
        @change="fetchYiJi"
      />
      <el-button type="primary" @click="fetchYiJi" :loading="loading">查询宜忌</el-button>
    </div>

    <div v-if="yiji" class="yiji-card">
      <div class="yiji-date">{{ yiji.date }}</div>
      <div class="yiji-section">
        <h3>宜</h3>
        <p class="yiji-text yi">{{ yiji.yi }}</p>
      </div>
      <div class="yiji-section">
        <h3>忌</h3>
        <p class="yiji-text ji">{{ yiji.ji }}</p>
      </div>
      <div v-if="yiji.summary" class="yiji-summary">{{ yiji.summary }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'

interface YiJi {
  date: string
  yi: string
  ji: string
  summary?: string
}

const selectedDate = ref('')
const loading = ref(false)
const yiji = ref<YiJi | null>(null)

function todayStr() {
  const d = new Date()
  return d.getFullYear() + '-' + String(d.getMonth() + 1).padStart(2, '0') + '-' + String(d.getDate()).padStart(2, '0')
}

async function fetchYiJi() {
  const date = selectedDate.value || todayStr()
  if (!date) return
  selectedDate.value = date
  loading.value = true
  yiji.value = null
  try {
    const res = await axios.get('/api/calendar/yiji', { params: { date } })
    if (res.data.code === 200 && res.data.data) {
      yiji.value = res.data.data
    }
  } catch (e) {
    console.error('获取宜忌失败:', e)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  selectedDate.value = todayStr()
  fetchYiJi()
})
</script>

<style scoped>
.calendar-page {
  max-width: 700px;
  margin: 0 auto;
}

.page-title {
  text-align: center;
  color: white;
  font-size: 32px;
  margin-bottom: 8px;
}

.subtitle {
  text-align: center;
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 24px;
}

.date-card {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.yiji-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.yiji-date {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #eee;
}

.yiji-section {
  margin-bottom: 16px;
}

.yiji-section h3 {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.yiji-text {
  margin: 0;
  line-height: 1.6;
  color: #333;
}

.yiji-text.yi { color: #67C23A; }
.yiji-text.ji { color: #E6A23C; }

.yiji-summary {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #eee;
  font-size: 14px;
  color: #666;
}
</style>
