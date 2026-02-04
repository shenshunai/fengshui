<template>
  <div class="bazi-page">
    <h1 class="page-title">八字排盘</h1>
    
    <!-- 输入表单 -->
    <div class="input-card">
      <el-form :model="form" label-width="80px">
        <div class="form-row">
          <el-form-item label="出生年">
            <el-input-number v-model="form.year" :min="1900" :max="2100" />
          </el-form-item>
          <el-form-item label="出生月">
            <el-input-number v-model="form.month" :min="1" :max="12" />
          </el-form-item>
          <el-form-item label="出生日">
            <el-input-number v-model="form.day" :min="1" :max="31" />
          </el-form-item>
          <el-form-item label="出生时">
            <el-input-number v-model="form.hour" :min="0" :max="23" />
          </el-form-item>
        </div>
        <el-form-item>
          <el-button type="primary" @click="calculate" :loading="loading">
            开始排盘
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 八字结果 -->
    <div v-if="result" class="result-card">
      <h2>八字命盘</h2>
      
      <!-- 四柱 -->
      <div class="pillars">
        <div class="pillar">
          <div class="pillar-label">年柱</div>
          <div class="pillar-value">{{ result.yearPillar }}</div>
        </div>
        <div class="pillar">
          <div class="pillar-label">月柱</div>
          <div class="pillar-value">{{ result.monthPillar }}</div>
        </div>
        <div class="pillar">
          <div class="pillar-label">日柱</div>
          <div class="pillar-value highlight">{{ result.dayPillar }}</div>
        </div>
        <div class="pillar">
          <div class="pillar-label">时柱</div>
          <div class="pillar-value">{{ result.hourPillar }}</div>
        </div>
      </div>
      
      <!-- 五行统计 -->
      <div class="five-elements">
        <h3>五行分布</h3>
        <div class="elements-bar">
          <div class="element-item metal">
            <div class="bar" :style="{ height: result.metalCount * 30 + 'px' }"></div>
            <span class="label">金</span>
            <span class="count">{{ result.metalCount }}</span>
          </div>
          <div class="element-item wood">
            <div class="bar" :style="{ height: result.woodCount * 30 + 'px' }"></div>
            <span class="label">木</span>
            <span class="count">{{ result.woodCount }}</span>
          </div>
          <div class="element-item water">
            <div class="bar" :style="{ height: result.waterCount * 30 + 'px' }"></div>
            <span class="label">水</span>
            <span class="count">{{ result.waterCount }}</span>
          </div>
          <div class="element-item fire">
            <div class="bar" :style="{ height: result.fireCount * 30 + 'px' }"></div>
            <span class="label">火</span>
            <span class="count">{{ result.fireCount }}</span>
          </div>
          <div class="element-item earth">
            <div class="bar" :style="{ height: result.earthCount * 30 + 'px' }"></div>
            <span class="label">土</span>
            <span class="count">{{ result.earthCount }}</span>
          </div>
        </div>
      </div>
      
      <!-- 命理分析 -->
      <div class="analysis">
        <h3>命理分析</h3>
        <div class="analysis-item">
          <span class="label">日主：</span>
          <span class="value">{{ result.dayMaster }}</span>
        </div>
        <div class="analysis-item">
          <span class="label">日主强弱：</span>
          <span class="value">{{ result.dayMasterStrength }}</span>
        </div>
        <div class="analysis-item">
          <span class="label">喜用神：</span>
          <span class="value favorable">{{ result.favorableElements }}</span>
        </div>
        <div class="analysis-item">
          <span class="label">忌神：</span>
          <span class="value unfavorable">{{ result.unfavorableElements }}</span>
        </div>
      </div>
      
      <!-- AI分析 -->
      <div class="ai-analysis">
        <h3>AI命理解读</h3>
        <pre>{{ result.aiAnalysis }}</pre>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

interface BaziResult {
  yearPillar: string
  monthPillar: string
  dayPillar: string
  hourPillar: string
  metalCount: number
  woodCount: number
  waterCount: number
  fireCount: number
  earthCount: number
  dayMaster: string
  dayMasterStrength: string
  favorableElements: string
  unfavorableElements: string
  aiAnalysis: string
}

const form = ref({
  year: 1990,
  month: 1,
  day: 1,
  hour: 12
})

const loading = ref(false)
const result = ref<BaziResult | null>(null)

const calculate = async () => {
  loading.value = true
  try {
    const res = await axios.get('/api/bazi/test', {
      params: form.value
    })
    if (res.data.code === 200) {
      result.value = res.data.data
      ElMessage.success('排盘成功')
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (error) {
    ElMessage.error('计算失败，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.bazi-page {
  max-width: 800px;
  margin: 0 auto;
}

.page-title {
  text-align: center;
  color: white;
  font-size: 32px;
  margin-bottom: 32px;
}

.input-card,
.result-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 24px;
}

.form-row {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.pillars {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin: 24px 0;
}

.pillar {
  text-align: center;
}

.pillar-label {
  font-size: 14px;
  color: #999;
  margin-bottom: 8px;
}

.pillar-value {
  font-size: 32px;
  font-weight: bold;
  padding: 16px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 12px;
}

.pillar-value.highlight {
  background: linear-gradient(135deg, #FF6B6B 0%, #FF8E53 100%);
}

.five-elements {
  margin: 24px 0;
}

.five-elements h3 {
  margin-bottom: 16px;
  color: #333;
}

.elements-bar {
  display: flex;
  justify-content: center;
  align-items: flex-end;
  height: 150px;
  gap: 32px;
}

.element-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.element-item .bar {
  width: 40px;
  border-radius: 4px 4px 0 0;
  transition: height 0.5s ease;
}

.element-item.metal .bar { background: linear-gradient(to top, #FFD700, #FFA500); }
.element-item.wood .bar { background: linear-gradient(to top, #32CD32, #228B22); }
.element-item.water .bar { background: linear-gradient(to top, #1E90FF, #0000CD); }
.element-item.fire .bar { background: linear-gradient(to top, #FF4500, #DC143C); }
.element-item.earth .bar { background: linear-gradient(to top, #8B4513, #A0522D); }

.element-item .label {
  font-size: 16px;
  font-weight: bold;
  margin-top: 8px;
}

.element-item .count {
  font-size: 12px;
  color: #999;
}

.analysis {
  padding: 16px;
  background: #f5f5f5;
  border-radius: 8px;
  margin-bottom: 24px;
}

.analysis h3 {
  margin-bottom: 16px;
  color: #333;
}

.analysis-item {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.analysis-item .label {
  width: 100px;
  color: #666;
}

.analysis-item .value {
  font-weight: bold;
}

.analysis-item .favorable {
  color: #67C23A;
}

.analysis-item .unfavorable {
  color: #F56C6C;
}

.ai-analysis {
  background: #fafafa;
  border-radius: 8px;
  padding: 16px;
}

.ai-analysis h3 {
  margin-bottom: 16px;
  color: #333;
}

.ai-analysis pre {
  white-space: pre-wrap;
  font-family: inherit;
  line-height: 1.8;
  color: #333;
}
</style>
