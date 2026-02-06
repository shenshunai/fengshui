<template>
  <div class="zodiac-page">
    <h1 class="page-title">星座运势</h1>
    
    <!-- 星座选择 -->
    <div class="zodiac-grid">
      <div 
        v-for="sign in zodiacSigns" 
        :key="sign.name"
        class="zodiac-item"
        :class="{ active: selectedSign === sign.name }"
        @click="selectSign(sign.name)"
      >
        <span class="zodiac-icon">{{ sign.icon }}</span>
        <span class="zodiac-name">{{ sign.name }}</span>
        <span class="zodiac-date">{{ sign.date }}</span>
      </div>
    </div>
    
    <!-- 运势展示 -->
    <div v-if="fortune" class="fortune-card">
      <div class="fortune-header">
        <h2>{{ fortune.zodiacSign }} 今日运势</h2>
        <span class="fortune-date">{{ fortune.date }}</span>
      </div>
      
      <div class="scores">
        <div class="score-item">
          <span class="label">综合运势</span>
          <el-progress :percentage="fortune.overallScore" :color="getScoreColor(fortune.overallScore)" />
        </div>
        <div class="score-item">
          <span class="label">爱情运势</span>
          <el-progress :percentage="fortune.loveScore" :color="getScoreColor(fortune.loveScore)" />
        </div>
        <div class="score-item">
          <span class="label">事业运势</span>
          <el-progress :percentage="fortune.careerScore" :color="getScoreColor(fortune.careerScore)" />
        </div>
        <div class="score-item">
          <span class="label">财运</span>
          <el-progress :percentage="fortune.wealthScore" :color="getScoreColor(fortune.wealthScore)" />
        </div>
        <div class="score-item">
          <span class="label">健康运势</span>
          <el-progress :percentage="fortune.healthScore" :color="getScoreColor(fortune.healthScore)" />
        </div>
      </div>
      
      <div class="lucky-info">
        <div class="lucky-item">
          <span class="label">幸运颜色</span>
          <span class="value">{{ fortune.luckyColor }}</span>
        </div>
        <div class="lucky-item">
          <span class="label">幸运数字</span>
          <span class="value">{{ fortune.luckyNumber }}</span>
        </div>
        <div class="lucky-item">
          <span class="label">幸运方位</span>
          <span class="value">{{ fortune.luckyDirection }}</span>
        </div>
      </div>
      
      <div class="fortune-content">
        <pre>{{ fortune.content }}</pre>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'

interface Fortune {
  zodiacSign: string
  date: string
  overallScore: number
  loveScore: number
  careerScore: number
  wealthScore: number
  healthScore: number
  luckyColor: string
  luckyNumber: number
  luckyDirection: string
  content: string
}

const zodiacSigns = [
  { name: '白羊座', icon: '♈', date: '3.21-4.19' },
  { name: '金牛座', icon: '♉', date: '4.20-5.20' },
  { name: '双子座', icon: '♊', date: '5.21-6.21' },
  { name: '巨蟹座', icon: '♋', date: '6.22-7.22' },
  { name: '狮子座', icon: '♌', date: '7.23-8.22' },
  { name: '处女座', icon: '♍', date: '8.23-9.22' },
  { name: '天秤座', icon: '♎', date: '9.23-10.23' },
  { name: '天蝎座', icon: '♏', date: '10.24-11.22' },
  { name: '射手座', icon: '♐', date: '11.23-12.21' },
  { name: '摩羯座', icon: '♑', date: '12.22-1.19' },
  { name: '水瓶座', icon: '♒', date: '1.20-2.18' },
  { name: '双鱼座', icon: '♓', date: '2.19-3.20' }
]

const selectedSign = ref('')
const fortune = ref<Fortune | null>(null)

const selectSign = async (sign: string) => {
  selectedSign.value = sign
  try {
    const res = await axios.get(`/api/zodiac/fortune/today/${sign}`)
    if (res.data.code === 200) {
      fortune.value = res.data.data
    }
  } catch (error) {
    console.error('获取运势失败:', error)
  }
}

const getScoreColor = (score: number) => {
  if (score >= 80) return '#67C23A'
  if (score >= 60) return '#E6A23C'
  return '#F56C6C'
}

onMounted(() => {
  selectSign('白羊座')
})
</script>

<style scoped>
.zodiac-page {
  max-width: 1000px;
  margin: 0 auto;
}

.page-title {
  text-align: center;
  color: white;
  font-size: 32px;
  margin-bottom: 32px;
}

.zodiac-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 12px;
  margin-bottom: 32px;
}

.zodiac-item {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  padding: 16px 8px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.zodiac-item:hover,
.zodiac-item.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.zodiac-icon {
  font-size: 24px;
  display: block;
  margin-bottom: 4px;
}

.zodiac-name {
  font-size: 14px;
  font-weight: bold;
  display: block;
}

.zodiac-date {
  font-size: 10px;
  opacity: 0.7;
}

.fortune-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 32px;
}

.fortune-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.fortune-header h2 {
  color: #333;
}

.fortune-date {
  color: #999;
}

.scores {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.score-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.score-item .label {
  width: 80px;
  color: #666;
}

.lucky-info {
  display: flex;
  gap: 32px;
  padding: 16px;
  background: #f5f5f5;
  border-radius: 8px;
  margin-bottom: 24px;
}

.lucky-item {
  display: flex;
  flex-direction: column;
}

.lucky-item .label {
  font-size: 12px;
  color: #999;
}

.lucky-item .value {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.fortune-content {
  background: #fafafa;
  border-radius: 8px;
  padding: 16px;
}

.fortune-content pre {
  white-space: pre-wrap;
  font-family: inherit;
  line-height: 1.8;
  color: #333;
}
</style>
