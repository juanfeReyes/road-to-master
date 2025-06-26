import { defineConfig } from 'vite'
import { reactRouter } from "@react-router/dev/vite";
import react from '@vitejs/plugin-react-swc'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
})
