<template>
  <el-card class="book-card" :body-style="{ padding: '15px' }">
    <div class="book-header">
      <h3>{{ book.title }}</h3>
      <el-tag :type="book.available ? 'success' : 'danger'">
        {{ book.available ? "可借阅" : "已借出" }}
      </el-tag>
    </div>

    <div class="book-meta">
      <p><strong>作者:</strong> {{ book.author }}</p>
      <p><strong>ISBN:</strong> {{ book.isbn }}</p>
      <p><strong>分类:</strong> {{ book.genre }}</p>
      <p><strong>出版日期:</strong> {{ formatDate(book.publicationDate) }}</p>
    </div>

    <div class="book-actions">
      <el-button type="primary" size="mini" @click="viewDetail(book.id)"
        >查看详情</el-button
      >

      <el-button
        v-if="book.available"
        type="success"
        size="mini"
        @click="borrowBook(book.id)"
        >借阅</el-button
      >

      <el-button v-else type="warning" size="mini" @click="returnBook(book.id)"
        >归还</el-button
      >

      <el-button type="danger" size="mini" @click="deleteBook(book.id)"
        >删除</el-button
      >
    </div>
  </el-card>
</template>

<script>
export default {
  props: {
    book: {
      type: Object,
      required: true,
    },
  },

  data() {
    return {
      borrowLoading: false,
      returnLoading: false,
      deleteLoading: false,
    };
  },
  methods: {
    viewDetail(id) {
      this.$router.push({ name: "book-detail", params: { id } });
    },

    async borrowBook(id) {
      this.borrowLoading = true;
      try {
        await this.$store.dispatch("book/borrowBook", id);
        this.$message.success("借阅成功！");
      } catch (error) {
        this.$message.error(
          `借阅失败: ${error.response?.data || error.message}`
        );
      } finally {
        this.borrowLoading = false;
      }
    },

    async returnBook(id) {
      try {
        await this.$store.dispatch("book/returnBook", id);
        this.$message.success("归还成功！");
      } catch (error) {
        this.$message.error(
          `归还失败: ${error.response?.data || error.message}`
        );
      }
    },

    async deleteBook(id) {
      try {
        await this.$confirm("确定要删除这本书吗？", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        });
        await this.$store.dispatch("book/deleteBook", id);
        this.$message.success("删除成功！");
      } catch (error) {
        if (error !== "cancel") {
          this.$message.error(
            `删除失败: ${error.response?.data || error.message}`
          );
        }
      }
    },

    formatDate(dateString) {
      if (!dateString) return "";
      return new Date(dateString).toLocaleDateString("zh-CN");
    },
  },
};
</script>

<style scoped>
.book-card {
  margin-bottom: 20px;
  transition: transform 0.3s;
}

.book-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.book-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.book-meta p {
  margin: 5px 0;
}

.book-actions {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>