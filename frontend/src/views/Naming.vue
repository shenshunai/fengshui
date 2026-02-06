<template>
  <div class="naming-page">
    <h1 class="page-title">起名</h1>
    <p class="subtitle">新生儿起名与名字打分（结合八字与 AI 推荐）</p>

    <el-tabs v-model="activeTab" class="naming-tabs">
      <el-tab-pane label="新生儿起名" name="generate">
        <div class="form-card">
          <el-form :model="genForm" label-width="100px" label-position="top">
            <el-form-item label="姓氏">
              <el-input v-model="genForm.surname" placeholder="如：王" maxlength="2" style="width: 120px" />
            </el-form-item>
            <el-form-item label="性别">
              <el-radio-group v-model="genForm.gender">
                <el-radio :value="1">男</el-radio>
                <el-radio :value="2">女</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="出生日期（选填，用于八字喜用）">
              <el-date-picker
                v-model="genForm.birthDate"
                type="date"
                placeholder="选择日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                style="width: 200px"
              />
            </el-form-item>
            <el-form-item label="推荐数量">
              <el-input-number v-model="genForm.count" :min="3" :max="12" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="generateNames" :loading="genLoading">生成名字</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="nameList.length" class="result-card">
          <h3>推荐名字</h3>
          <div class="name-list">
            <div v-for="(item, i) in nameList" :key="i" class="name-item">
              <span class="name-text">{{ item.name }}</span>
              <el-tag type="success" size="small"> {{ item.score }} 分</el-tag>
              <span class="name-analysis">{{ item.analysis }}</span>
            </div>
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="名字打分" name="score">
        <div class="form-card">
          <el-form :model="scoreForm" label-width="100px" label-position="top">
            <el-form-item label="您的姓名">
              <el-input v-model="scoreForm.name" placeholder="输入 2-4 个字" maxlength="4" style="width: 200px" />
            </el-form-item>
            <el-form-item label="出生日期（选填，用于八字参考）">
              <el-date-picker
                v-model="scoreForm.birthDate"
                type="date"
                placeholder="选择日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                style="width: 200px"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="scoreName" :loading="scoreLoading">查看评分</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div v-if="scoreResult" class="result-card score-result">
          <h3>{{ scoreResult.name }} — {{ scoreResult.score }} 分</h3>
          <p class="score-summary">{{ scoreResult.summary }}</p>
          <ul v-if="scoreResult.details?.length" class="score-details">
            <li v-for="(d, i) in scoreResult.details" :key="i">{{ d }}</li>
          </ul>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import axios from 'axios'

interface NameItem {
  name: string
  score: number
  analysis: string
}

interface ScoreResult {
  name: string
  score: number
  summary: string
  details: string[]
}

const activeTab = ref('generate')
const genLoading = ref(false)
const scoreLoading = ref(false)
const nameList = ref<NameItem[]>([])
const scoreResult = ref<ScoreResult | null>(null)

const genForm = reactive({
  surname: '王',
  gender: 1 as number,
  birthDate: '' as string,
  count: 6 as number
})

const scoreForm = reactive({
  name: '',
  birthDate: '' as string
})

function parseDate(str: string): { year: number; month: number; day: number } | null {
  if (!str) return null
  const [y, m, d] = str.split('-').map(Number)
  if (!y || !m || !d) return null
  return { year: y, month: m, day: d }
}

async function generateNames() {
  genLoading.value = true
  nameList.value = []
  try {
    const birth = parseDate(genForm.birthDate)
    const payload = {
      surname: genForm.surname || '王',
      gender: genForm.gender,
      count: genForm.count,
      year: birth?.year,
      month: birth?.month,
      day: birth?.day,
      isLunar: false
    }
    const res = await axios.post('/api/naming/generate', payload)
    if (res.data.code === 200 && Array.isArray(res.data.data)) {
      nameList.value = res.data.data
    }
  } catch (e) {
    console.error('起名失败:', e)
  } finally {
    genLoading.value = false
  }
}

async function scoreName() {
  if (!scoreForm.name?.trim()) return
  scoreLoading.value = true
  scoreResult.value = null
  try {
    const birth = parseDate(scoreForm.birthDate)
    const payload = {
      name: scoreForm.name.trim(),
      year: birth?.year,
      month: birth?.month,
      day: birth?.day,
      isLunar: false
    }
    const res = await axios.post('/api/naming/score', payload)
    if (res.data.code === 200 && res.data.data) {
      scoreResult.value = res.data.data
    }
  } catch (e) {
    console.error('打分失败:', e)
  } finally {
    scoreLoading.value = false
  }
}
</script>

<style scoped>
.naming-page {
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

.naming-tabs {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.form-card {
  margin-bottom: 24px;
}

.result-card {
  margin-top: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 12px;
}

.result-card h3 {
  margin: 0 0 12px 0;
  color: #333;
}

.name-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.name-item {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.name-text {
  font-size: 18px;
  font-weight: bold;
  min-width: 80px;
}

.name-analysis {
  color: #666;
  font-size: 14px;
}

.score-result .score-summary {
  margin: 0 0 12px 0;
  color: #333;
}

.score-details {
  margin: 0;
  padding-left: 20px;
  color: #666;
  font-size: 14px;
}
</style>
