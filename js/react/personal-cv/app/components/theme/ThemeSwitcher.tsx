
import { useEffect, useState, type PropsWithChildren } from 'react'
import './theme.module.scss'
import './colors.module.scss'
import { FaRegMoon } from 'react-icons/fa';
import { LuSun } from 'react-icons/lu';

const mode = {
  LIGHT: 'LIGHT',
  DARK: 'DARK'
} as const

type modeType = keyof typeof mode;

interface ThemeContextProps { }
export const ThemeSwitcher = ({ }: ThemeContextProps) => {
  const [mode, setMode] = useState<modeType>('LIGHT')

  const handleSwitch = () => {
    setMode((mode) => mode === 'LIGHT' ? 'DARK':'LIGHT')
  }

  useEffect(() => {
    document.documentElement.setAttribute('data-theme', mode === 'LIGHT' ? 'light':'dark')
  }, [mode])

  return (<div>
    <button onClick={handleSwitch}>
      {mode === 'LIGHT' ? <LuSun /> : <FaRegMoon />}
    </button>
  </div>)
}
