<template>
  <div class="app">
    <nav class="navbar">
      <div class="nav-brand">
        <span class="logo">☯</span>
        <span class="brand-name">玄学平台</span>
      </div>
      <div class="nav-links">
        <router-link to="/">首页</router-link>
        <router-link to="/zodiac">星座运势</router-link>
        <router-link to="/bazi">八字排盘</router-link>
        <router-link to="/fengshui">风水</router-link>
        <router-link to="/calendar">万年历</router-link>
        <router-link to="/naming">起名</router-link>
        <router-link to="/divine">抽签占卜</router-link>
      </div>
    </nav>
    
    <main class="main-content">
      <router-view />
    </main>
    
    <footer class="footer">
      <p>© 2024 玄学平台 - 星座运势 | 八字算命 | 风水测算</p>
    </footer>
  </div>
</template>

<script setup lang="ts">
// App组件
</script>

<style scoped>
.app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.navbar {
  background: rgba(255, 255, 255, 0.95);
  padding: 16px 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.nav-brand {
  display: flex;
  align-items: center;
  gap: 8px;
}

.logo {
  font-size: 32px;
}

.brand-name {
  font-size: 20px;
  font-weight: bold;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.nav-links {
  display: flex;
  gap: 24px;
}

.nav-links a {
  text-decoration: none;
  color: #333;
  font-weight: 500;
  padding: 8px 16px;
  border-radius: 8px;
  transition: all 0.3s;
}

.nav-links a:hover,
.nav-links a.router-link-active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.main-content {
  flex: 1;
  padding: 32px;
}

.footer {
  background: rgba(0, 0, 0, 0.3);
  color: white;
  text-align: center;
  padding: 16px;
}
</style>
