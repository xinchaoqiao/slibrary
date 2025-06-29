<template>
  <el-form 
    ref="bookForm" 
    :model="book" 
    :rules="rules" 
    label-width="100px"
    label-position="top"
  >
    <el-form-item label="书名" prop="title">
      <el-input v-model="book.title" placeholder="请输入书名" />
    </el-form-item>
    
    <el-form-item label="作者" prop="author">
      <el-input v-model="book.author" placeholder="请输入作者" />
    </el-form-item>
    
    <el-form-item label="ISBN" prop="isbn">
      <el-input v-model="book.isbn" placeholder="请输入ISBN号" />
    </el-form-item>
    
    <el-form-item label="分类" prop="genre">
      <el-select v-model="book.genre" placeholder="请选择分类">
        <el-option
          v-for="genre in genres"
          :key="genre"
          :label="genre"
          :value="genre"
        />
      </el-select>
    </el-form-item>
    
    <el-form-item label="出版日期" prop="publicationDate">
      <el-date-picker
        v-model="book.publicationDate"
        type="date"
        placeholder="选择出版日期"
        value-format="yyyy-MM-dd"
      />
    </el-form-item>
    
    <el-form-item>
      <el-button type="primary" @click="submitForm">提交</el-button>
      <el-button @click="resetForm">重置</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  props: {
    initialBook: {
      type: Object,
      default: () => ({
        title: '',
        author: '',
        isbn: '',
        genre: '',
        publicationDate: ''
      })
    }
  },
  data() {
    return {
      book: { ...this.initialBook },
      genres: [
        'FICTION', 'NON_FICTION', 'SCIENCE_FICTION', 'FANTASY', 
        'MYSTERY', 'ROMANCE', 'HISTORY', 'BIOGRAPHY', 'SCIENCE', 
        'TECHNOLOGY', 'ART', 'TRAVEL', 'COOKING', 'SELF_HELP', 'CHILDREN'
      ],
      rules: {
        title: [
          { required: true, message: '请输入书名', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        author: [
          { required: true, message: '请输入作者', trigger: 'blur' },
          { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        isbn: [
          { required: true, message: '请输入ISBN号', trigger: 'blur' },
          { pattern: /^[0-9-]{10,17}$/, message: 'ISBN格式不正确', trigger: 'blur' }
        ],
        genre: [
          { required: true, message: '请选择分类', trigger: 'change' }
        ],
        publicationDate: [
          { required: true, message: '请选择出版日期', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    initialBook: {
      immediate: true,
      handler(newVal) {
        this.book = { ...newVal }
      }
    }
  },
  methods: {
    submitForm() {
      this.$refs.bookForm.validate(valid => {
        if (valid) {
          this.$emit('submit', this.book)
        } else {
          return false
        }
      })
    },
    
    resetForm() {
      this.$refs.bookForm.resetFields()
    }
  }
}
</script>