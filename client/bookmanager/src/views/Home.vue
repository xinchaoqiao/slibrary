<template>
  <div class="home-container">
    <div class="welcome-section">
      <h1>欢迎来到图书管理系统</h1>
      <p class="subtitle">高效管理您的图书资源，实现借阅与归还一体化</p>
      <el-button type="primary" size="medium" @click="goToBookList">
        开始管理图书 <i class="el-icon-arrow-right"></i>
      </el-button>
    </div>

    <div class="feature-section">
      <h2>核心功能</h2>
      <div class="features">
        <el-card class="feature-card">
          <div class="feature-icon">
            <i class="el-icon-notebook-2"></i>
          </div>
          <h3>图书管理</h3>
          <p>添加、编辑、删除图书信息，完善图书资料库</p>
        </el-card>

        <el-card class="feature-card">
          <div class="feature-icon">
            <i class="el-icon-search"></i>
          </div>
          <h3>图书搜索</h3>
          <p>快速检索图书，支持书名、作者、ISBN等多条件搜索</p>
        </el-card>

        <el-card class="feature-card">
          <div class="feature-icon">
            <i class="el-icon-refresh"></i>
          </div>
          <h3>借阅管理</h3>
          <p>一键借阅与归还，实时更新图书状态</p>
        </el-card>
      </div>
    </div>

    <div class="stats-section" v-if="stats">
      <h2>系统统计</h2>
      <div class="stats">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.totalBooks }}</div>
          <div class="stat-label">总图书数量</div>
        </el-card>

        <el-card class="stat-card">
          <div class="stat-value">{{ stats.availableBooks }}</div>
          <div class="stat-label">可借阅图书</div>
        </el-card>

        <el-card class="stat-card">
          <div class="stat-value">{{ stats.borrowedBooks }}</div>
          <div class="stat-label">已借出图书</div>
        </el-card>
      </div>
    </div>

    <div class="recent-books" v-if="recentBooks.length > 0">
      <h2>最近添加的图书</h2>
      <div class="recent-list">
        <book-card 
          v-for="book in recentBooks" 
          :key="book.id" 
          :book="book" 
          class="recent-book-item"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import BookCard from '@/components/BookCard.vue'

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Home',
  components: { BookCard },
  data() {
    return {
      stats: null
    }
  },
  computed: {
    ...mapState('book', ['books']),
    
    recentBooks() {
      // 获取最近添加的5本书
      return [...this.books]
        .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
        .slice(0, 5)
    }
  },
  created() {
    this.fetchStats()
  },
  methods: {
    ...mapActions('book', ['fetchBooks']),
    
    async fetchStats() {
      try {
        // 先获取所有图书
        await this.fetchBooks()
        
        // 计算统计数据
        this.stats = {
          totalBooks: this.books.length,
          availableBooks: this.books.filter(book => book.available).length,
          borrowedBooks: this.books.filter(book => !book.available).length
        }
      } catch (error) {
        console.error('获取统计信息失败:', error)
      }
    },
    
    goToBookList() {
      this.$router.push({ name: 'book-list' })
    }
  }
}
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.welcome-section {
  text-align: center;
  padding: 50px 20px;
  background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
  border-radius: 10px;
  color: white;
  margin-bottom: 40px;
}

.welcome-section h1 {
  font-size: 2.5rem;
  margin-bottom: 15px;
}

.subtitle {
  font-size: 1.2rem;
  max-width: 600px;
  margin: 0 auto 30px;
}

.feature-section {
  margin-bottom: 40px;
}

.feature-section h2, .stats-section h2, .recent-books h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
}

.features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.feature-card {
  text-align: center;
  transition: transform 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  font-size: 48px;
  color: #409EFF;
  margin-bottom: 15px;
}

.feature-icon i {
  line-height: 1;
}

.feature-card h3 {
  margin-bottom: 10px;
}

.stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.stat-card {
  text-align: center;
  padding: 20px;
}

.stat-value {
  font-size: 2.5rem;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 10px;
}

.stat-label {
  color: #909399;
  font-size: 1rem;
}

.recent-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.recent-book-item {
  transition: all 0.3s;
}

.recent-book-item:hover {
  transform: scale(1.03);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}
</style>