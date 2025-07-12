

import { useTranslation } from 'react-i18next'
import styles from './banner.module.scss'
import { FaDownload, FaLinkedin } from 'react-icons/fa'

export const Banner = () => {
  const { t, i18n } = useTranslation()

  return (<div className={`${styles['banner-bg']} ${styles['bg-color']} ${styles.layout}`}>
    <img src='https://placehold.co/600x400' alt='juan image' />
    <div className={`${styles.content}`}>
      <div className={`${styles['title-container']}`}>
        <h1 className={`${styles.title}`}>Juan Felipe Reyes</h1>
        <p className={`${styles.subtitle}`}>{t('banner.roleTitle')}</p>
      </div>
      <div className={`${styles.contact_container}`}>
        <a href='../linkToPDF' target='_blank'><FaDownload /></a>
        <a href='https://www.linkedin.com/in/juan-felipe-reyes-garcia/' target='_blank'><FaLinkedin /></a>
      </div>
    </div>
  </div>)
}
