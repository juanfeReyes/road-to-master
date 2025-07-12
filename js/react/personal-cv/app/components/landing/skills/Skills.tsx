import { useTranslation } from "react-i18next"
import { SkillCard } from "./SkillCard"
import type { Skill } from "~/domain/Skill"

import styles from './skill.module.scss'

const skills: Skill[] = [
  {
    name: 'JAVA',
    level: 5,
    categories: ['BACK_DEV']
  },
  {
    name: "JAVASCRIPT",
    level: 4,
    categories: ['FRONT_DEV']
  },
  {
    name: 'REACT',
    level: 3,
    categories: []
  },
  {
    name: 'RUST',
    level: 2,
    categories: []
  },
  {
    name: 'SPRING',
    level: 5,
    categories: []
  },
  {
    name: 'SQL',
    level: 3,
    categories: []
  },
  {
    name: 'TYPESCRIPT',
    level: 4,
    categories: []
  },
  {
    name: 'AWS_CLOUD',
    level: 2,
    categories: []
  },
  {
    name: 'CLEAN_ARCHITECTURE',
    level: 4,
    categories: []
  },
  {
    name: 'CYPRESS',
    level: 5,
    categories: []
  },
  {
    name: 'DOMAIN_DRIVEN_DESIGN',
    level: 4,
    categories: []
  },
  {
    name: 'KAFKA',
    level: 3,
    categories: []
  },
  {
    name: 'MARIA_DB',
    level: 3,
    categories: []
  },
  {
    name: 'MONGO_DB',
    level: 4,
    categories: []
  },
  {
    name: 'REDIS',
    level: 3,
    categories: []
  },
  {
    name: 'SCRUM',
    level: 5,
    categories: []
  },
  {
    name: 'SPRING_BATCH',
    level: 4,
    categories: []
  },
  {
    name: 'SPRING_BOOT',
    level: 5,
    categories: []
  }
]

export const Skills = () => {
  const { t } = useTranslation()

  return (<div className={`${styles.list_container}`}>
    <h2 className={`${styles.title}`}>{t('skills.title')}</h2>
    <div className={`${styles.horizontal_layout}`}>
      {skills
      .sort((a, b) => b.level - a.level)
      .sort((a, b) => b.name > a.name)
      .map((skill) => (<SkillCard skill={skill} />))}
    </div>
  </div>)
}
