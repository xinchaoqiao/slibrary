/* eslint-disable no-undef */
/* eslint-disable no-useless-catch */
import bookService from '@/services/bookService'
// 图书模块状态管理


// 添加日期格式化工具函数
function formatDate(date) {
    if (!date) return null;

    // 如果已经是字符串格式，直接返回
    if (typeof date === 'string') return date;

    // 如果是日期对象，格式化为 yyyy-MM-dd
    if (date instanceof Date) {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

    // 其他情况返回空字符串
    return '';
}
export default {
    namespaced: true,
    state: {
        books: [],
        currentBook: null,
        searchResults: []
    },
    mutations: {
        SET_BOOKS(state, books) {
            state.books = books
        },
        SET_CURRENT_BOOK(state, book) {
            state.currentBook = book
        },
        SET_SEARCH_RESULTS(state, results) {
            state.searchResults = results
        },
        ADD_BOOK(state, book) {
            // 格式化日期
            const formattedBook = {
                ...book,
                publicationDate: formatDate(book.publicationDate)
            };

            state.books.push(formattedBook);
        },

        UPDATE_BOOK(state, updatedBook) {
            const index = state.books.findIndex(b => b.id === updatedBook.id)
            if (index !== -1) {
                // 格式化日期字段 - 直接使用顶部定义的 formatDate 函数
                const formattedBook = {
                    ...updatedBook,
                    updatedAt: formatDate(updatedBook.updatedAt) // 修改此处，移除 this.
                }
                // 使用数组的 splice 方法替代 Vue.set (Vue 3 中不再需要 Vue.set)
                state.books.splice(index, 1, formattedBook)

                // 如果当前图书就是被更新的图书，也更新它
                if (state.currentBook && state.currentBook.id === updatedBook.id) {
                    state.currentBook = formattedBook
                }
            }
        },

        REMOVE_BOOK(state, id) {
            state.books = state.books.filter(book => book.id !== id)
        },

    },
    actions: {
        async fetchAvailableBooks({ commit }) {
            try {
                console.log('Vuex: 开始获取可借阅图书');
                const books = await bookService.getAvailableBooks();

                // 验证数据
                if (!books || books.length === 0) {
                    console.warn('Vuex: 未获取到可借阅图书');
                }

                commit('SET_BOOKS', books);
                return books;
            } catch (error) {
                console.error('Vuex: 获取可借阅图书失败', error);
                throw error; // 抛出错误以便组件处理
            }
        },
        async fetchBooks({ commit }) {
            try {
                const response = await bookService.getAllBooks()
                commit('SET_BOOKS', response.data)
                return response.data
            } catch (error) {
                throw error
            }
        },

        async searchBooks({ commit }, keyword) {
            try {
                const response = await bookService.searchBooks(keyword)
                commit('SET_SEARCH_RESULTS', response.data)
                return response.data
            } catch (error) {
                throw error
            }
        },

        async fetchBookById({ commit }, id) {
            try {
                const response = await bookService.getBookById(id)
                commit('SET_CURRENT_BOOK', response.data)
                return response.data
            } catch (error) {
                throw error
            }
        },

        async createBook({ commit }, book) {
            try {
                const response = await bookService.addBook(book)
                commit('ADD_BOOK', response.data)
                return response.data
            } catch (error) {
                throw error
            }
        },

        async updateBook({ commit }, { id, book }) {
            try {
                const response = await bookService.updateBook(id, book)
                commit('UPDATE_BOOK', response.data)
                return response.data
            } catch (error) {
                throw error
            }
        },

        async deleteBook({ commit }, id) {
            try {
                await bookService.deleteBook(id)
                commit('REMOVE_BOOK', id)
            } catch (error) {
                throw error
            }
        },

        async borrowBook({ commit }, id) {
            try {
                const response = await bookService.borrowBook(id)
                commit('UPDATE_BOOK', response.data)
                return response.data
            } catch (error) {
                throw error
            }
        },

        async returnBook({ commit }, id) {
            try {
                const response = await bookService.returnBook(id)
                commit('UPDATE_BOOK', response.data)
                return response.data
            } catch (error) {
                throw error
            }
        }
    }
}