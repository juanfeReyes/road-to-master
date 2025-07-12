
import i18n from 'i18next'
import { initReactI18next } from 'react-i18next'
import es from './languages/es.json'
import en from './languages/en.json'

const resources = {
  es: {
    translation: {...es}
  },
  en: {
    translation: {...en}
  },
}

i18n
.use(initReactI18next)
.init({
  resources: resources,
  lng: 'es',
})

export default i18n;
