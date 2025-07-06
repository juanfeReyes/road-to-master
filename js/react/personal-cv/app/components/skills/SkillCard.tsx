import { FaCircle, FaJava, FaJs, FaReact, FaRegCircle, FaRust } from "react-icons/fa"
import type { Skill, SkillNameType } from "~/domain/Skill"

import styles from './skill.module.scss'
import { useTranslation } from "react-i18next"
import { BsPatchQuestion } from "react-icons/bs"
import { SiSpring, SiTypescript } from "react-icons/si"
import { PiFileSql } from "react-icons/pi"

interface SkilLevelProps {
  level: number
}

const SkillLevel = ({ level }: SkilLevelProps) => {
  const { t } = useTranslation()

  return (<div >
    {t(`skill.level.${level}`)}
    <div className={`${styles.horizontal_layout}`}>
      {Array(level).fill(0).map(() => <FaCircle />)}
      {Array(5 - level).fill(0).map(() => <FaRegCircle />)}
    </div>
  </div>)
}

interface SkillIconProps {
  name: SkillNameType
}

const SkillIcon = ({ name }: SkillIconProps) => {
  switch (name) {
    case 'JAVA':
      return <FaJava className={`${styles.icon}`} />
    case 'JAVASCRIPT':
      return <FaJs className={`${styles.icon}`} />
    case 'REACT':
      return <FaReact className={`${styles.icon}`} />
    case 'RUST':
      return <FaRust className={`${styles.icon}`} />
    case 'SPRING':
      return <SiSpring className={`${styles.icon}`} />
    case 'SQL':
      return <PiFileSql className={`${styles.icon}`} />
    case 'TYPESCRIPT':
      return <SiTypescript className={`${styles.icon}`} />
    default:
      return <BsPatchQuestion className={`${styles.icon}`} />
  }
}

export interface SkillCardProps {
  skill: Skill
}

export const SkillCard = ({ skill }: SkillCardProps) => {
  const {t} = useTranslation()

  return (<div className={`${styles.card}`}>
    <div className={`${styles.horizontal_layout} ${styles.header}`}>
      <SkillIcon name={skill.name} />
      <h4>{t(`skill.name.${skill.name}`)}</h4>
    </div>
    <SkillLevel level={skill.level} />
  </div>)
}
