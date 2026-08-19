import { defineConfig } from 'vitest/config'
import { playwright } from '@vitest/browser-playwright'
import react from '@vitejs/plugin-react'
import tsconfigPaths from 'vite-tsconfig-paths'
import tailwindcss from '@tailwindcss/vite'


export default defineConfig({
  plugins: [react(), tsconfigPaths(), tailwindcss(),],
  publicDir: 'public', 
  test: {
    browser: {
      enabled: true,
      headless: true,
      provider: playwright(),
      // https://vitest.dev/config/browser/playwright
      instances: [
        { browser: 'chromium' },
      ],
    },
    setupFiles: ['./lib/test/browser-setup.ts'],
    coverage: {
      enabled: true,
      provider: 'v8', // or 'istanbul'
      reporter: ['text', 'html'],
      include: ['{src,lib,components,app}/**/*.{ts,tsx,js,jsx}'], 
      exclude: ['src/**/*.d.ts', '**/test/**', '**/mocks/**', '**/app/**']
    },
  },
  define: {
    'process.env': {},
  },
})
