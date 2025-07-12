const SkillCategory = {
  BACK_DEV: 'BACK_DEV',
  FRONT_DEV: 'FRONT_DEV',
  AUTOMATION_ENG: 'AUTOMATION_ENG',
} as const

export type SkillCategoryType = keyof typeof SkillCategory

const SkillName  = {
  JAVA: 'JAVA',
  JAVASCRIPT: 'JAVASCRIPT',
  SPRING: 'SPRING',
  SPRING_BOOT: 'SPRING_BOOT',
  SPRING_BATCH: 'SPRING_BATCH',
  REACT: 'REACT',
  TYPESCRIPT: 'TYPESCRIPT',
  SQL: 'SQL',
  RUST: 'RUST',
  NODE: 'NODE',
  AWS_CLOUD: 'AWS_CLOUD',
  MARIA_DB: 'MARIA_DB',
  MONGO_DB: 'MONGO_DB',
  REDIS: 'REDIS',
  KAFKA: 'KAFKA',
  CYPRESS: 'CYPRESS',
  SCRUM: 'SCRUM',
  DOMAIN_DRIVEN_DESIGN: 'DOMAIN_DRIVEN_DESIGN',
  CLEAN_ARCHITECTURE: 'CLEAN_ARCHITECTURE',
} as const

export type SkillNameType = keyof typeof SkillName

export interface Skill {
  name: SkillNameType,
  level: number,
  categories: SkillCategoryType[]
}
