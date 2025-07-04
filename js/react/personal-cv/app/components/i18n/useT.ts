import { useTranslation } from "react-i18next"
import { usei18nLanguage } from "./i18nStore"
import i18n from "./i18nConfig"

export const useT = () => {
  const lang = usei18nLanguage()
  return useTranslation()
}
