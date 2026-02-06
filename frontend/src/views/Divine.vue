<template>
  <div class="divine-page">
    <h1 class="page-title">抽签占卜</h1>
    
    <div class="divine-types">
      <div 
        v-for="type in divineTypes" 
        :key="type.id"
        class="divine-card"
        @click="selectType(type)"
      >
        <div class="divine-icon">{{ type.icon }}</div>
        <h3>{{ type.name }}</h3>
        <p>{{ type.description }}</p>
      </div>
    </div>
    
    <!-- 抽签区域 -->
    <div v-if="selectedType" class="draw-section">
      <div class="draw-card">
        <h2>{{ selectedType.name }}</h2>
        <p class="hint">心中默念所求之事，点击下方抽签</p>
        
        <div class="draw-area" @click="drawSign" :class="{ drawing: isDrawing }">
          <div class="sign-container">
            <div v-if="!signResult" class="sign-placeholder">
              {{ isDrawing ? '正在抽签...' : '点击抽签' }}
            </div>
            <div v-else class="sign-result">
              <div class="sign-number">第 {{ signResult.number }} 签</div>
              <div class="sign-level" :class="signResult.level">{{ signResult.level }}</div>
              <div class="sign-poem">{{ signResult.poem }}</div>
            </div>
          </div>
        </div>
        
        <div v-if="signResult" class="interpretation">
          <h3>签文解读</h3>
          <p>{{ signResult.interpretation }}</p>
          
          <div class="advice-section">
            <div class="advice-item">
              <span class="label">事业：</span>
              <span>{{ signResult.career }}</span>
            </div>
            <div class="advice-item">
              <span class="label">感情：</span>
              <span>{{ signResult.love }}</span>
            </div>
            <div class="advice-item">
              <span class="label">财运：</span>
              <span>{{ signResult.wealth }}</span>
            </div>
          </div>
          
          <el-button type="primary" @click="resetSign">重新抽签</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

interface DivineType {
  id: string
  name: string
  icon: string
  description: string
}

interface SignResult {
  number: number
  level: string
  poem: string
  interpretation: string
  career: string
  love: string
  wealth: string
}

const divineTypes: DivineType[] = [
  { id: 'guanyin', name: '观音灵签', icon: '🙏', description: '观世音菩萨灵感签，指引迷津' },
  { id: 'yuebao', name: '月老签', icon: '❤️', description: '月下老人姻缘签，问情问爱' },
  { id: 'fortune', name: '财神签', icon: '💰', description: '财神爷财运签，求财问运' },
  { id: 'career', name: '文昌签', icon: '📚', description: '文昌帝君学业签，问学问业' }
]

// 签文库
const signDatabase = {
  levels: ['上上签', '上签', '中签', '下签'],
  poems: [
    '春来花开满园香，好运连连喜洋洋。贵人相助事业旺，心想事成福满堂。',
    '云开雾散见青天，守得云开见月明。但行好事莫问前，自有贵人来相逢。',
    '风平浪静好行船，稳扎稳打步步前。不急不躁心自安，时来运转在眼前。',
    '山重水复疑无路，柳暗花明又一村。莫道眼前多困难，转机就在不远处。'
  ],
  interpretations: [
    '此签大吉，诸事顺遂。求官得官，求财得财，所求皆如愿以偿。',
    '此签为吉，运势向好。虽有小阻，终能化解，宜积极进取。',
    '此签平平，凡事宜守。不宜冒进，静待时机，自有转机。',
    '此签需谨慎，暂时不宜妄动。宜修身养性，积蓄力量，待时而发。'
  ]
}

const selectedType = ref<DivineType | null>(null)
const isDrawing = ref(false)
const signResult = ref<SignResult | null>(null)

const selectType = (type: DivineType) => {
  selectedType.value = type
  signResult.value = null
}

const drawSign = async () => {
  if (isDrawing.value || signResult.value) return
  
  isDrawing.value = true
  
  // 模拟抽签动画
  await new Promise(resolve => setTimeout(resolve, 2000))
  
  // 生成随机签文
  const randomIndex = Math.floor(Math.random() * 4)
  signResult.value = {
    number: Math.floor(Math.random() * 100) + 1,
    level: signDatabase.levels[randomIndex],
    poem: signDatabase.poems[randomIndex],
    interpretation: signDatabase.interpretations[randomIndex],
    career: randomIndex < 2 ? '事业运势向好，可积极拓展' : '事业宜守不宜进，待时而动',
    love: randomIndex < 2 ? '感情顺遂，有佳音传来' : '感情需耐心经营，勿急于求成',
    wealth: randomIndex < 2 ? '财运亨通，正财偏财皆有' : '财运平平，宜节俭持家'
  }
  
  isDrawing.value = false
}

const resetSign = () => {
  signResult.value = null
}
</script>

<style scoped>
.divine-page {
  max-width: 1000px;
  margin: 0 auto;
}

.page-title {
  text-align: center;
  color: white;
  font-size: 32px;
  margin-bottom: 32px;
}

.divine-types {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 32px;
}

.divine-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 24px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.divine-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
}

.divine-icon {
  font-size: 48px;
  margin-bottom: 12px;
}

.divine-card h3 {
  color: #333;
  margin-bottom: 8px;
}

.divine-card p {
  color: #666;
  font-size: 14px;
}

.draw-section {
  margin-top: 32px;
}

.draw-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 32px;
  text-align: center;
}

.draw-card h2 {
  color: #333;
  margin-bottom: 8px;
}

.hint {
  color: #999;
  margin-bottom: 24px;
}

.draw-area {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 48px;
  margin-bottom: 24px;
  cursor: pointer;
  transition: transform 0.3s;
}

.draw-area:hover {
  transform: scale(1.02);
}

.draw-area.drawing {
  animation: shake 0.5s infinite;
}

@keyframes shake {
  0%, 100% { transform: rotate(-2deg); }
  50% { transform: rotate(2deg); }
}

.sign-container {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 32px;
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.sign-placeholder {
  color: white;
  font-size: 24px;
}

.sign-result {
  color: white;
  text-align: center;
}

.sign-number {
  font-size: 20px;
  opacity: 0.8;
  margin-bottom: 8px;
}

.sign-level {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 16px;
}

.sign-level.上上签 { color: #FFD700; }
.sign-level.上签 { color: #90EE90; }
.sign-level.中签 { color: white; }
.sign-level.下签 { color: #FFA07A; }

.sign-poem {
  font-size: 16px;
  line-height: 1.8;
}

.interpretation {
  text-align: left;
  padding: 24px;
  background: #f5f5f5;
  border-radius: 12px;
}

.interpretation h3 {
  color: #333;
  margin-bottom: 12px;
}

.interpretation > p {
  color: #666;
  line-height: 1.8;
  margin-bottom: 16px;
}

.advice-section {
  background: white;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
}

.advice-item {
  margin-bottom: 8px;
}

.advice-item .label {
  font-weight: bold;
  color: #333;
}
</style>
