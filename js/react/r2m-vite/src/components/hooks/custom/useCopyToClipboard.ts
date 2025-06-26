import { useState } from "react"

export const useCopyToClipboard = (initialState = '') => {
  const [copiedText, setcopiedText] = useState('')

  const copy = (text: string) => {
    navigator.clipboard.writeText(text);
  }

  return [copiedText, copy] as const
}
