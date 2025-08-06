import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  // Development server configuration
  server: {
    port: 3000,
    proxy: {
      // Forward API requests to Spring Boot server
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
  // Production build configuration
  build: {
    outDir: '../resources/static',
    emptyOutDir: true,
    sourcemap: true,
  },
  // Environment variables
  envPrefix: 'REACT_APP_',
})
