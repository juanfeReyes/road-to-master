import { useTranslation } from "react-i18next"
import { SkillCard } from "./SkillCard"
import type { Skill } from "~/domain/Skill"

import styles from './skill.module.scss'

const skills: Skill[] = [
  {
    name: 'JAVA',
    level: 3,
    categories: ['BACK_DEV']
  },
  {
    name: "JAVASCRIPT",
    level: 3,
    categories: ['FRONT_DEV']
  },
  {
    name: 'REACT',
    level: 3,
    categories: []
  },
  {
    name: 'RUST',
    level: 3,
    categories: []
  },
  {
    name: 'SPRING',
    level: 3,
    categories: []
  },
  {
    name: 'SQL',
    level: 3,
    categories: []
  },
  {
    name: 'TYPESCRIPT',
    level: 3,
    categories: []
  },

]

export const Skills = () => {
  const { t } = useTranslation()

  return (<div className={`${styles.list_container}`}>
    <h2 className={`${styles.title}`}>{t('skills.title')}</h2>
    <div className={`${styles.horizontal_layout}`}>
      {skills.map((skill) => (<SkillCard skill={skill} />))}
    </div>
  </div>)
}
