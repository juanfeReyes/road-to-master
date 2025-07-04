

import { useTranslation } from 'react-i18next'
import { useT } from '../i18n/useT'
import styles from './banner.module.scss'
import i18n from '../i18n/i18nConfig'

export const Banner = () => {
  const {t, i18n} = useTranslation()

  return (<div className={`${styles['banner-bg']} ${styles['bg-color']}`}>
    <img src='https://placehold.co/600x400' alt='juan image'/>
    <h1>Juan Felipe Reyes</h1>
    <p>{t('banner.roleTitle')}</p>
  </div>)
}
