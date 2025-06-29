import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/views/Home.vue'
import BookList from '@/views/BookList.vue'
import AddBook from '@/views/AddBook.vue'
import BookDetail from '@/views/BookDetail.vue'

Vue.use(Router)

export default new Router({
  routes: [
    { path: '/', name: 'home', component: Home },
    { path: '/books', name: 'book-list', component: BookList },
    { path: '/books/add', name: 'add-book', component: AddBook },
    { path: '/books/:id', name: 'book-detail', component: BookDetail, props: true }
  ]
})