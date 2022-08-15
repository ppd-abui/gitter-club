import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import axios from "axios";

export default defineConfig({
  plugins: [vue()],
  publicPath:'./',
  server: {
    port: 8080,
    proxy: {                 //设置代理，必须填
      '/api': {              //设置拦截器  拦截器格式   斜杠+拦截器名字，名字可以自己定
        target: 'http://localhost:9090',     //代理的目标地址
        changeOrigin: true,              //是否设置同源，输入是的
        rewrite: (path) => path.replace(/^\/api/,'')
      }
    }
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
})
