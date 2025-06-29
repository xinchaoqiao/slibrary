<template>
  <div class="add-book-container">
    <el-page-header @back="goBack" content="添加新书" />
    
    <el-card class="form-card">
      <h2>填写图书信息</h2>
      <book-form ref="bookForm" @submit="handleSubmit" />
    </el-card>
  </div>
</template>

<script>
import BookForm from '@/components/BookForm.vue'
import { mapActions } from 'vuex'

export default {
  components: { BookForm },
  methods: {
    ...mapActions('book', ['createBook']),
    
    goBack() {
      this.$router.go(-1)
    },
    
    async handleSubmit(bookData) {
      try {
        // 直接提交原始数据，store 会处理格式化
        await this.createBook(bookData)
        this.$message.success('图书添加成功！')
        this.$router.push({ name: 'book-list' })
      } catch (error) {
        console.error('添加图书错误:', error)
        this.$message.error(`添加失败: ${error.response?.data || error.message}`)
      }
    }
  }
}
</script>

<style scoped>
.add-book-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.form-card {
  margin-top: 20px;
  padding: 30px;
}

.form-card h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
}
</style>