import { useCallback, useState } from "react"

export const useSwitch = (initialState = false) => {
  const [toggle, setToggle] = useState(initialState)

  const swtich = useCallback(() => {
    setToggle(toggle => !toggle)
  }, [])

  return [toggle, swtich] as const
}
