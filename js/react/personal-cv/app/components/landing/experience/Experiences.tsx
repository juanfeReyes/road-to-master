import { useTranslation } from "react-i18next"
import { ExperienceCard } from "./ExperienceCard"
import styles from './experience.module.scss'
import { FaKeyboard } from "react-icons/fa"

const experiences: Experience[] = [
  {
    company: 'Perficient',
    startDate: new Date(2019, 7, 8),
    roles: [
      {
        name: 'TechnicalConsultant',
        startDate: new Date(2019, 7, 8),
      },
      {
        name: 'SeniorSoftwareEngineer',
        startDate: new Date(2023, 0, 8),
      },
      {
        name: 'TechnicalLead',
        startDate: new Date(2021, 0, 8),
      }
    ]
  }
]

export const Experiences = () => {
  const { t } = useTranslation()


  return (<div className={`${styles.experience_list} ${styles.container_bg}`}>
    <h2 className={`${styles.title}`}>{t('experience.title')}</h2>
    <div className={`${styles.horizontal_container} ${styles.justify_end}`}>
        {experiences
          .sort((a, b) => (a.startDate.getMilliseconds() - b.startDate.getMilliseconds()))
          .map((experience) => <ExperienceCard experience={experience} />)}
    </div>
  </div>)
}
