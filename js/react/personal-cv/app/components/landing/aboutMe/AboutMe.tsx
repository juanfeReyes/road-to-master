import { useTranslation } from "react-i18next"

import styles from './aboutme.module.scss'

export const AboutMe = () => {

  const {t} = useTranslation()

  return (<div className={`${styles.container}`}>
    <h2 className={`${styles.title}`}>{t('aboutme.title')}</h2>
    <p className={`${styles.description}`}>{t('aboutme.description')}</p>
    <img src="https://placehold.co/250" alt="about me image"/>
  </div>)
}
