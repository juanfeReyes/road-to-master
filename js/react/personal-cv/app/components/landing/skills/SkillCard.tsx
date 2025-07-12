import { FaAws, FaCircle, FaJava, FaJs, FaNode, FaReact, FaRegCircle, FaRust } from "react-icons/fa"
import type { Skill, SkillNameType } from "~/domain/Skill"

import styles from './skill.module.scss'
import { useTranslation } from "react-i18next"
import { BsHexagonFill, BsPatchQuestion } from "react-icons/bs"
import { SiApachekafka, SiCypress, SiMariadb, SiMaterialdesignicons, SiMongodb, SiSpring, SiSpringboot, SiTypescript } from "react-icons/si"
import { PiFileSql } from "react-icons/pi"
import { DiRedis, DiScrum } from "react-icons/di"
import { MdBatchPrediction } from "react-icons/md"

interface SkilLevelProps {
  level: number
}

const SkillLevel = ({ level }: SkilLevelProps) => {
  const { t } = useTranslation()

  return (<div >
    {t(`skill.level.${level}`)}
    <div className={`${styles.level}`}>
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
    case 'TYPESCRIPT':
      return <SiTypescript className={`${styles.icon}`} />
    case 'SQL':
      return <PiFileSql className={`${styles.icon}`} />
    case 'AWS_CLOUD':
      return <FaAws className={`${styles.icon}`} />
    case 'CLEAN_ARCHITECTURE':
      return <BsHexagonFill className={`${styles.icon}`} />
    case 'CYPRESS':
      return <SiCypress className={`${styles.icon}`} />
    case 'DOMAIN_DRIVEN_DESIGN':
      return <SiMaterialdesignicons className={`${styles.icon}`} />
    case 'KAFKA':
      return <SiApachekafka className={`${styles.icon}`} />
    case 'MARIA_DB':
      return <SiMariadb className={`${styles.icon}`} />
    case 'MONGO_DB':
      return <SiMongodb className={`${styles.icon}`} />
    case 'NODE':
      return <FaNode className={`${styles.icon}`} />
    case 'REDIS':
      return <DiRedis className={`${styles.icon}`} />
    case 'SCRUM':
      return <DiScrum className={`${styles.icon}`} />
    case 'SPRING_BATCH':
      return <MdBatchPrediction className={`${styles.icon}`} />
    case 'SPRING_BOOT':
      return <SiSpringboot className={`${styles.icon}`} />
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
