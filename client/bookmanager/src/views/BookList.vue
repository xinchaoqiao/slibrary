<template>
  <div class="book-list-container">
    <div class="header-section">
      <h1>图书管理</h1>
      <div class="action-buttons">
        <el-button type="primary" icon="el-icon-plus" @click="goToAddBook">
          添加新书
        </el-button>
        <el-button type="success" @click="fetchAvailableBooks">
          查看可借阅图书
        </el-button>
      </div>
    </div>

    <div class="search-section">
      <el-input
        v-model="searchKeyword"
        placeholder="输入书名、作者或ISBN搜索"
        clearable
        @input="handleSearch"
        @clear="handleClear"  
      >
        <template #append>
          <el-button icon="el-icon-search" @click="handleSearch" />
        </template>
      </el-input>
    </div>  <!-- 关闭search-section div -->

    <div class="filter-section">
      <el-select
        v-model="filterGenre"
        placeholder="按分类筛选"
        clearable
        @change="filterBooks"
      >
        <el-option
          v-for="genre in genres"
          :key="genre"
          :label="genre"
          :value="genre"
        />
      </el-select>

      <el-radio-group v-model="filterAvailability" @change="filterBooks">
        <el-radio-button label="all">全部</el-radio-button>
        <el-radio-button label="available">可借阅</el-radio-button>
        <el-radio-button label="borrowed">已借出</el-radio-button>
      </el-radio-group>
    </div>

    <div v-loading="loading" class="books-grid">
      <!-- 显示错误信息 -->
      <div v-if="errorMessage" class="error-message">
        <el-alert :title="errorMessage" type="error" show-icon />
        <el-button type="primary" @click="fetchBooks">重试</el-button>  <!-- 修改这里：重试时调用fetchBooks -->
      </div>

      <template v-else-if="displayedBooks.length > 0">
        <book-card
          v-for="book in displayedBooks"
          :key="book.id"
          :book="book"
          class="book-item"
        />
      </template>

      <el-empty v-else description="没有找到图书" />
    </div>

    <div v-if="displayedBooks.length > 0" class="pagination">
      <el-pagination
        :current-page="currentPage"
        :page-size="pageSize"
        :total="filteredBooks.length"
        layout="prev, pager, next"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import BookCard from "@/components/BookCard.vue";

export default {
  components: { BookCard },
  data() {
    return {
      searchKeyword: "",
      filterGenre: "",
      filterAvailability: "all",
      currentPage: 1,
      pageSize: 12,
      loading: false,
      genres: [
        "FICTION",
        "NON_FICTION",
        "SCIENCE_FICTION",
        "FANTASY",
        "MYSTERY",
        "ROMANCE",
        "HISTORY",
        "BIOGRAPHY",
        "SCIENCE",
        "TECHNOLOGY",
        "ART",
        "TRAVEL",
        "COOKING",
        "SELF_HELP",
        "CHILDREN",
      ],
      errorMessage: null,
    };
  },
  computed: {
    ...mapState("book", ["books", "searchResults"]),

    filteredBooks() {
      let result = this.searchKeyword ? this.searchResults : this.books;

      if (this.filterGenre) {
        result = result.filter((book) => book.genre === this.filterGenre);
      }

      if (this.filterAvailability === "available") {
        result = result.filter((book) => book.available);
      } else if (this.filterAvailability === "borrowed") {
        result = result.filter((book) => !book.available);
      }

      return result;
    },

    displayedBooks() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredBooks.slice(start, end);
    },
  },
  created() {
    this.fetchBooks();
  },
  methods: {
    ...mapActions("book", ["fetchBooks", "searchBooks", "fetchAvailableBooks"]),

    // 修改后的fetchBooks方法
  async fetchBooks() {
    this.loading = true;
    this.errorMessage = null;
    try {
      // 添加可用性过滤参数
      const params = {
        availableOnly: this.filterAvailability === "available"
      };
      await this.$store.dispatch("book/fetchBooks", params);
    } catch (error) {
      this.$message.error("获取图书失败: " + error.message);
    } finally {
      this.loading = false;
    }
  },

    // 新增方法：处理清除搜索框
    handleClear() {
      this.searchKeyword = "";
      this.fetchBooks();
      this.currentPage = 1;
    },

    handleSearch() {
      if (this.searchKeyword.trim()) {
        this.$store.dispatch("book/searchBooks", this.searchKeyword);
      } else {
        this.fetchBooks();
      }
      this.currentPage = 1;
    },

    filterBooks() {
      this.currentPage = 1;
    },

    handlePageChange(page) {
      this.currentPage = page;
      window.scrollTo(0, 0);
    },

    goToAddBook() {
      this.$router.push({ name: "add-book" });
    },

    // 修改后的fetchAvailableBooks方法
  async fetchAvailableBooks() {
    this.loading = true;
    this.errorMessage = null;
    this.searchKeyword = "";
    this.filterAvailability = "available";
    
    try {
      // 直接调用fetchBooks并设置可用性过滤
      await this.$store.dispatch("book/fetchBooks", {
        availableOnly: true
      });
      this.currentPage = 1;
    } catch (error) {
      console.error("获取可借阅图书失败:", error);
      this.errorMessage = `获取可借阅图书失败: ${error.message}`;
      this.$message.error(this.errorMessage);
    } finally {
      this.loading = false;
    }
  },
  },
};
</script>

<style scoped>
.error-message {
  grid-column: 1 / -1;
  text-align: center;
  padding: 20px;
}

.error-message .el-alert {
  margin-bottom: 15px;
}

.book-list-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.search-section {
  margin-bottom: 20px;
}

.filter-section {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  align-items: center;
}

.books-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>