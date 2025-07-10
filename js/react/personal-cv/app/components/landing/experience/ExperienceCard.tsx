import { useMemo } from "react"
import { useTranslation } from "react-i18next"
import styles from './experience.module.scss'

const getYearsOfExperience = (date: Date) => new Date().getFullYear() - date.getFullYear()

interface RoleSummaryProps {
  role: JobRole
}

export const RoleSummary = ({ role }: RoleSummaryProps) => {
  const { t } = useTranslation()

  return (<div className={`${styles.item}`}>
    <hgroup className={`${styles.header}`}>
      <h5>{t(`experience.role.${role.name}`)}</h5>
      <h6>{role.startDate.toDateString()}</h6>
    </hgroup>
    <p>{t(`experience.role.${role.name}.description`)}</p>
  </div>)
}

interface ExperienceCardProps {
  experience: Experience
}

export const ExperienceCard = ({ experience }: ExperienceCardProps) => {
  const { t } = useTranslation()
  const expTime = useMemo(() => getYearsOfExperience(experience.startDate), [])

  return (<div className={`${styles.container_bg} ${styles.card}`}>
    <hgroup className={`${styles.horizontal_container} ${styles.justify_between} ${styles.title}`}>
      <h4>{experience.company}</h4>
      <h6>{expTime} {t('label.years')}</h6>
    </hgroup>
    <div>
      <div className={`${styles.list}`}>
        {experience.roles
          .sort((a, b) => (b.startDate - a.startDate))
          .map((role) => <RoleSummary role={role} />)}
      </div>
    </div>
  </div>)
}
