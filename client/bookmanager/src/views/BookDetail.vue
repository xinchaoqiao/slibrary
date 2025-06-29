<template>
  <div class="book-detail-container" v-loading="loading">
    <el-page-header @back="goBack" content="图书详情" />
    
    <div v-if="book" class="book-detail">
      <div class="book-header">
        <h1>{{ book.title }}</h1>
        <el-tag :type="book.available ? 'success' : 'danger'" size="medium">
          {{ book.available ? '可借阅' : '已借出' }}
        </el-tag>
      </div>
      
      <div class="book-info">
        <div class="book-meta">
          <p><strong>作者:</strong> {{ book.author }}</p>
          <p><strong>ISBN:</strong> {{ book.isbn }}</p>
          <p><strong>分类:</strong> {{ book.genre }}</p>
          <p><strong>出版日期:</strong> {{ formatDate(book.publicationDate) }}</p>
          <p><strong>添加时间:</strong> {{ formatDateTime(book.createdAt) }}</p>
          <p><strong>最后更新:</strong> {{ formatDateTime(book.updatedAt) }}</p>
        </div>
        
        <div class="book-actions">
          <el-button 
            v-if="book.available" 
            type="primary" 
            size="medium" 
            @click="borrowBook"
          >
            <i class="el-icon-takeaway-box"></i> 借阅图书
          </el-button>
          
          <el-button 
            v-else 
            type="success" 
            size="medium" 
            @click="returnBook"
          >
            <i class="el-icon-box"></i> 归还图书
          </el-button>
          
          <el-button 
            type="warning" 
            size="medium" 
            @click="editBook"
          >
            <i class="el-icon-edit"></i> 编辑信息
          </el-button>
          
          <el-button 
            type="danger" 
            size="medium" 
            @click="deleteBook"
          >
            <i class="el-icon-delete"></i> 删除图书
          </el-button>
        </div>
      </div>
      
      <div v-if="isEditing" class="edit-form">
        <h2>编辑图书信息</h2>
        <book-form 
          :initial-book="book" 
          @submit="updateBook"
          @cancel="isEditing = false"
        />
      </div>
    </div>
    
    <el-empty v-else description="图书不存在或已删除" />
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import BookForm from '@/components/BookForm.vue'

export default {
  components: { BookForm },
  props: {
    id: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      book: null,
      loading: false,
      isEditing: false
    }
  },
  created() {
    this.fetchBook()
  },
  methods: {
    ...mapActions('book', ['fetchBookById', 'borrowBook', 'returnBook', 'updateBook', 'deleteBook']),
    
    async fetchBook() {
      this.loading = true
      try {
        const book = await this.$store.dispatch('book/fetchBookById', this.id)
        this.book = book
      } catch (error) {
        this.$message.error('获取图书详情失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    goBack() {
      this.$router.go(-1)
    },
    
    formatDate(dateString) {
      if (!dateString) return ''
      return new Date(dateString).toLocaleDateString('zh-CN')
    },
    
    formatDateTime(dateTimeString) {
      if (!dateTimeString) return ''
      return new Date(dateTimeString).toLocaleString('zh-CN')
    },
    
    async borrowBook() {
      try {
        await this.$store.dispatch('book/borrowBook', this.id)
        this.book.available = false
        this.$message.success('借阅成功！')
      } catch (error) {
        this.$message.error(`借阅失败: ${error.response?.data || error.message}`)
      }
    },
    
    async returnBook() {
      try {
        await this.$store.dispatch('book/returnBook', this.id)
        this.book.available = true
        this.$message.success('归还成功！')
      } catch (error) {
        this.$message.error(`归还失败: ${error.response?.data || error.message}`)
      }
    },
    
    editBook() {
      this.isEditing = true
    },
    
    async updateBook(bookData) {
      try {
        await this.$store.dispatch('book/updateBook', {
          id: this.id,
          book: bookData
        })
        this.book = { ...this.book, ...bookData }
        this.isEditing = false
        this.$message.success('图书信息更新成功！')
      } catch (error) {
        this.$message.error(`更新失败: ${error.response?.data || error.message}`)
      }
    },
    
    async deleteBook() {
      try {
        await this.$confirm('确定要删除这本书吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await this.$store.dispatch('book/deleteBook', this.id)
        this.$message.success('图书删除成功！')
        this.$router.push({ name: 'book-list' })
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error(`删除失败: ${error.response?.data || error.message}`)
        }
      }
    }
  }
}
</script>

<style scoped>
.book-detail-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.book-detail {
  margin-top: 20px;
}

.book-header {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 25px;
}

.book-info {
  display: flex;
  gap: 40px;
  margin-bottom: 30px;
}

.book-meta {
  flex: 1;
}

.book-meta p {
  margin: 10px 0;
  font-size: 16px;
}

.book-actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
  min-width: 200px;
}

.edit-form {
  margin-top: 30px;
  padding: 20px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  background-color: #fafafa;
}
</style>