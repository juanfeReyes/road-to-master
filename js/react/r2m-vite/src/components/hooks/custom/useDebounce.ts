import { useEffect, useState } from "react"
import { useSwitch } from "./useSwitch"

export const useDebounce = <T>(value: T, delay = 500) => {
  const [debounceValue, setDebounceValue] = useState(value)
  const [timer, switchTimer] = useSwitch(true)

  useEffect(() => {
    if(timer){
      setDebounceValue(value)
      switchTimer()
    }
    const timeout = setTimeout(() => {
      switchTimer()
    }, delay)

    return () => clearTimeout(timeout)
  }, [value, delay])

  return debounceValue

}
