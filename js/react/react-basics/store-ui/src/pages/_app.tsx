import { ThemeToggler } from '@/components/shared/ThemeManager/ThemeToggler'
import { store } from '@/store/ReduxStore'
import type { AppProps } from 'next/app'
import { Provider } from 'react-redux'

export default function App({ Component, pageProps }: AppProps) {
  return <>
    <Provider store={store}>
      <ThemeToggler>
        <Component {...pageProps} />
      </ThemeToggler >
    </Provider>
  </>
}
