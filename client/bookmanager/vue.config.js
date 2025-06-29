module.exports = {
    devServer: {
        proxy: {
            '/api': {
                target: 'http://localhost:8080', // 后端地址
                changeOrigin: true,
                pathRewrite: {
                    '^/api': '' // 移除路径中的 /api
                }
            }
        },
        // 允许的主机头，设置为 '0.0.0.0' 表示允许所有主机
        historyApiFallback: true,
        allowedHosts: "all",
        client: {
            // 根据环境动态设置 WebSocket URL
            webSocketURL: process.env.NODE_ENV === 'production'
                ? 'wss://618d-2409-8a44-914-9374-94b1-46f-f5e8-9d8.ngrok-free.app/ws' // 生产环境（ngrok）
                : 'ws://localhost:8081/ws' // 开发环境
        }
    }
}