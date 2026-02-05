<template>
  <div class="fengshui-page">
    <h1 class="page-title">风水常识</h1>
    <p class="subtitle">常用风水规则与宜忌，供参考</p>

    <div class="ai-card">
      <div class="ai-header">
        <span>✨ AI 风水解读（由 ChatGPT 生成）</span>
        <el-button type="primary" size="small" @click="fetchAiContent" :loading="aiLoading">
          {{ aiContent ? '重新生成' : '获取 AI 解读' }}
        </el-button>
      </div>
      <div v-if="aiContent" class="ai-content">{{ aiContent }}</div>
      <div v-else-if="!aiLoading" class="ai-placeholder">点击上方按钮，获取更丰富、个性化的风水建议</div>
    </div>

    <div class="rules-card">
      <el-collapse v-model="activeNames">
        <el-collapse-item title="一、大门与玄关" name="door">
          <ul>
            <li><strong>大门不对镜：</strong>大门正对镜子易将财气反射出去，不宜在正对大门处放镜。</li>
            <li><strong>大门不对厕：</strong>大门直对卫生间易犯污秽，可用屏风或绿植遮挡。</li>
            <li><strong>玄关宜亮：</strong>玄关宜明亮整洁，象征前途光明；可设小灯常亮。</li>
            <li><strong>鞋柜高度：</strong>鞋柜不宜过高，以不超过成人肩高为宜，避免压运。</li>
          </ul>
        </el-collapse-item>
        <el-collapse-item title="二、客厅" name="living">
          <ul>
            <li><strong>沙发有靠：</strong>沙发宜背靠实墙，象征有靠山，不宜背门或背窗。</li>
            <li><strong>梁不压顶：</strong>沙发、床、书桌上方不宜有横梁，可用吊顶或柜体化解。</li>
            <li><strong>财位宜静：</strong>明财位（大门对角线顶端）宜放绿植或吉祥物，忌杂乱、重物。</li>
            <li><strong>客厅宜亮：</strong>客厅主阳，宜光线充足，阴暗易影响家运。</li>
          </ul>
        </el-collapse-item>
        <el-collapse-item title="三、卧室" name="bedroom">
          <ul>
            <li><strong>床不对门：</strong>床头不宜正对房门，易受冲；脚不对门为佳。</li>
            <li><strong>床不对镜：</strong>床不宜正对镜子，夜间易受惊；镜不对床即可。</li>
            <li><strong>床头有靠：</strong>床头宜靠实墙，不宜靠窗或悬空。</li>
            <li><strong>卧室宜简：</strong>卧室不宜过多电器、杂物，利于休息与感情稳定。</li>
          </ul>
        </el-collapse-item>
        <el-collapse-item title="四、厨房与餐厅" name="kitchen">
          <ul>
            <li><strong>灶不对门：</strong>灶台不宜正对厨房门，易漏财；背后宜有靠。</li>
            <li><strong>水火不相冲：</strong>灶台与水池不宜正对或紧邻，水火相冲不利。</li>
            <li><strong>厨房宜整洁：</strong>厨房主财库，整洁明亮利于聚财。</li>
            <li><strong>餐桌宜圆或方：</strong>圆桌聚气，方桌稳重；避免尖角桌。</li>
          </ul>
        </el-collapse-item>
        <el-collapse-item title="五、卫生间" name="bathroom">
          <ul>
            <li><strong>厕不居中：</strong>卫生间不宜在房屋正中，易污秽中宫。</li>
            <li><strong>门常关：</strong>卫生间门宜常关，避免秽气外泄。</li>
            <li><strong>保持干燥：</strong>卫生间宜通风干燥，可放绿植或香氛改善气场。</li>
          </ul>
        </el-collapse-item>
        <el-collapse-item title="六、书房与办公" name="study">
          <ul>
            <li><strong>书桌有靠：</strong>书桌宜背靠实墙，面向门口或窗，前途开阔。</li>
            <li><strong>左高右低：</strong>桌面左手边可稍高（如书架），象征青龙高于白虎。</li>
            <li><strong>不宜背窗：</strong>背窗而坐易背后无靠，不利事业。</li>
          </ul>
        </el-collapse-item>
        <el-collapse-item title="七、植物与摆件" name="items">
          <ul>
            <li><strong>旺财植物：</strong>发财树、金钱树、富贵竹等宜放财位或玄关。</li>
            <li><strong>带刺植物：</strong>仙人掌、玫瑰等不宜放室内，可放阳台化煞。</li>
            <li><strong>鱼缸方位：</strong>鱼缸不宜放财位（易见财化水），可放凶位化煞。</li>
            <li><strong>神像摆放：</strong>神像宜面向大门或开阔处，背后有靠，不宜对厕对灶。</li>
          </ul>
        </el-collapse-item>
        <el-collapse-item title="八、通用宜忌" name="general">
          <ul>
            <li><strong>路冲：</strong>大门或窗正对直路、楼梯、电梯口为路冲，可用屏风、绿植化解。</li>
            <li><strong>尖角煞：</strong>对窗、对门的建筑尖角可用绿植、帘子或凸镜化解。</li>
            <li><strong>穿堂风：</strong>大门直通后门或窗为穿堂，易散财，宜设玄关或屏风。</li>
            <li><strong>整洁为要：</strong>家中整洁、光线充足、空气流通是基本要求。</li>
          </ul>
        </el-collapse-item>
      </el-collapse>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const activeNames = ref(['door', 'living', 'bedroom'])
const aiContent = ref('')
const aiLoading = ref(false)

async function fetchAiContent() {
  aiLoading.value = true
  aiContent.value = ''
  try {
    const res = await axios.get('/api/fengshui/content')
    if (res.data.code === 200 && res.data.data?.content) {
      aiContent.value = res.data.data.content
      ElMessage.success('已获取 AI 解读')
    } else {
      ElMessage.warning('暂未配置 AI 服务或生成失败，请查看下方常用规则')
    }
  } catch {
    ElMessage.warning('AI 服务未启动或网络异常，请查看下方常用规则')
  } finally {
    aiLoading.value = false
  }
}
</script>

<style scoped>
.fengshui-page {
  max-width: 800px;
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
  font-size: 14px;
  margin-bottom: 24px;
}

.ai-card {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 24px;
  color: white;
}

.ai-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.ai-content {
  white-space: pre-wrap;
  line-height: 1.8;
  font-size: 14px;
  opacity: 0.95;
}

.ai-placeholder {
  font-size: 14px;
  opacity: 0.8;
}

.rules-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 24px;
}

.rules-card ul {
  margin: 0;
  padding-left: 20px;
  line-height: 2;
  color: #333;
}

.rules-card li {
  margin-bottom: 12px;
}

.rules-card strong {
  color: #667eea;
}
</style>
