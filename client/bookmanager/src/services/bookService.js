import axios from 'axios'

// 创建带错误处理的axios实例
const apiClient = axios.create({
    // baseURL: 'http://39.96.167.208:8080', // 修改为完整后端地址
    baseURL:'/api',
    timeout: 8000,
    headers: {
        'Content-Type': 'application/json',
    }
})

// 添加请求拦截器
apiClient.interceptors.request.use(config => {
    console.log(`请求: ${config.method.toUpperCase()} ${config.url}`);
    return config;
}, error => {
    console.error('请求错误:', error);
    return Promise.reject(error);
});

// 添加响应拦截器
apiClient.interceptors.response.use(response => {
    console.log(`响应: ${response.status} ${response.config.url}`);
    return response;
}, error => {
    console.error('响应错误:', error);

    let errorMessage = '网络错误';

    if (error.response) {
        errorMessage = `服务器错误: ${error.response.status} ${error.response.statusText}`;
    } else if (error.request) {
        errorMessage = '未收到服务器响应';
    } else {
        errorMessage = `请求配置错误: ${error.message}`;
    }

    return Promise.reject(new Error(errorMessage));
});

export default {
    // 获取所有图书
    getAllBooks() {
        return apiClient.get('/books')
    },

    // 搜索图书
    searchBooks(keyword) {
        return apiClient.get(`/books/search?keyword=${keyword}`)
    },

    // 获取图书详情
    getBookById(id) {
        return apiClient.get(`/books/${id}`)
    },

    // 添加新书
    addBook(book) {
        const bookWithTimestamp = {
            ...book,
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString()
        }
        return apiClient.post('/books', bookWithTimestamp)
    },

    // 更新图书
    updateBook(id, book) {
        const bookWithTimestamp = {
            ...book,
            updatedAt: new Date().toISOString()
        }
        return apiClient.put(`/books/${id}`, bookWithTimestamp)
    },

    // 删除图书
    deleteBook(id) {
        return apiClient.delete(`/books/${id}`)
    },

    // 借阅图书
    borrowBook(id) {
        return apiClient.post(`/books/${id}/borrow`)
    },

    // 归还图书
    returnBook(id) {
        return apiClient.post(`/books/${id}/return`)
    },

    // 获取可借阅图书
    getAvailableBooks() {
        return apiClient.get('/books/available')
            .then(response => {
                if (!Array.isArray(response.data)) {
                    throw new Error('无效的响应格式');
                }
                return response.data;
            });
    },
}