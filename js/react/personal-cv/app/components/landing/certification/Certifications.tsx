import { useTranslation } from "react-i18next"

import styles from './certifications.module.scss'
import { FiExternalLink } from "react-icons/fi"

const certifications = [
  "AWS Cloud Practitioner - CLF-C02",
  "Professional Scrum Master I - PSM",
  "Software development engineering, ICESI University 2020"
]

const researchs = [
  {
    label: 'LSTM and Convolution Networks exploration for Parkinson’s Diagnosis, IEEE Xplore, 2019',
    link: 'https://ieeexplore.ieee.org/document/8809160'
  }
]

export const Certifications = () => {

  const { t } = useTranslation()

  return (<div className={`${styles.certification_container}`}>
    <h2 className={`${styles.title}`}>{t('certification.title')}</h2>
    <div className={`${styles.list}`}>
      <div className={`${styles.content}`}>
        <ul>
          {certifications.map((cert) => <li>{cert}</li>)}
        </ul>
        <div>
          <h3 className={`${styles.title}`}>{t('certification.investigation.title')}</h3>
          <ul >
            {researchs.map((research) => <li>
              <p>
                {research.label}&nbsp;
                <a href={research.link} target="_blank">
                  <FiExternalLink className={`${styles.link}`} />
                </a>
              </p>
            </li>
            )}
          </ul>
        </div>
      </div>
      <img src="https://placehold.co/400x300" alt="certifications" />
    </div>
  </div>)
}

