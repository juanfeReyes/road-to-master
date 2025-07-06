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
  REACT: 'REACT',
  TYPESCRIPT: 'TYPESCRIPT',
  SQL: 'SQL',
  RUST: 'RUST',
} as const

export type SkillNameType = keyof typeof SkillName

export interface Skill {
  name: SkillNameType,
  level: number,
  categories: SkillCategoryType[]
}
