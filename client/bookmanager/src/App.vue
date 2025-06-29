<template>
  <div id="app">
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header>
        <el-menu 
          mode="horizontal" 
          :default-active="activeIndex" 
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b"
          router
        >
          <el-menu-item index="/">首页</el-menu-item>
          <el-menu-item index="/books">图书管理</el-menu-item>
          <el-menu-item index="/books/add">添加图书</el-menu-item>
        </el-menu>
      </el-header>
      
      <!-- 主内容区 -->
      <el-main>
        <router-view />
      </el-main>
      
      <!-- 页脚 -->
      <el-footer>
        <div class="footer-content">
          <p>图书管理系统 © {{ new Date().getFullYear() }} 版权所有</p>
          <p>技术支持: Name:Joe.  email:xinchaoqiao6@163.com</p>
        </div>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App',
  computed: {
    activeIndex() {
      return this.$route.path
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.el-header {
  padding: 0;
  background-color: #545c64;
}

.el-main {
  flex: 1;
  padding: 20px;
}

.el-footer {
  background-color: #f5f7fa;
  border-top: 1px solid #e6e6e6;
  text-align: center;
  padding: 15px 0;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  color: #909399;
  font-size: 0.9rem;
}

.footer-content p {
  margin: 5px 0;
}
</style>