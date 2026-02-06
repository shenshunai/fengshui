<template>
  <div class="home">
    <div class="hero">
      <h1>探索命运的奥秘</h1>
      <p>星座运势 · 八字算命 · 风水测算 · 起名服务</p>
    </div>
    
    <div class="features">
      <div class="feature-card" @click="$router.push('/zodiac')">
        <div class="icon">⭐</div>
        <h3>星座运势</h3>
        <p>每日、每周、每月运势分析，把握人生方向</p>
      </div>
      
      <div class="feature-card" @click="$router.push('/bazi')">
        <div class="icon">☯</div>
        <h3>八字排盘</h3>
        <p>精准八字命理分析，洞悉命运密码</p>
      </div>
      
      <div class="feature-card" @click="$router.push('/fengshui')">
        <div class="icon">🏠</div>
        <h3>风水测算</h3>
        <p>家居风水布局，趋吉避凶</p>
      </div>

      <div class="feature-card" @click="$router.push('/calendar')">
        <div class="icon">📅</div>
        <h3>万年历</h3>
        <p>今日宜忌，择吉避凶</p>
      </div>

      <div class="feature-card" @click="$router.push('/naming')">
        <div class="icon">✍️</div>
        <h3>起名</h3>
        <p>新生儿起名与名字打分</p>
      </div>
      
      <div class="feature-card" @click="$router.push('/divine')">
        <div class="icon">🎴</div>
        <h3>抽签占卜</h3>
        <p>观音灵签、月老签，解答心中疑惑</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// Home组件
</script>

<style scoped>
.home {
  max-width: 1200px;
  margin: 0 auto;
}

.hero {
  text-align: center;
  padding: 60px 20px;
  color: white;
}

.hero h1 {
  font-size: 48px;
  margin-bottom: 16px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
}

.hero p {
  font-size: 20px;
  opacity: 0.9;
}

.features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 24px;
  padding: 20px;
}

.feature-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  padding: 32px;
  text-align: center;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.feature-card h3 {
  font-size: 20px;
  color: #333;
  margin-bottom: 8px;
}

.feature-card p {
  color: #666;
  font-size: 14px;
}
</style>
