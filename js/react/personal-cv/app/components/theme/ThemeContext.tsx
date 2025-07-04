
import type { PropsWithChildren } from 'react'
import styles from './theme.module.scss'


interface ThemeContextProps extends PropsWithChildren {}
export const ThemeContext = ({children}: ThemeContextProps) => {

  return (<div>
    {children}
  </div>)
}
