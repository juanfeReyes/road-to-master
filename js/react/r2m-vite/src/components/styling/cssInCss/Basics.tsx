
import { useState } from 'react'
import styles from './basics.module.scss'
import { IoIosStar } from 'react-icons/io'

const initialState = [
  {name: 'react', level: 3},
  {name: 'java', level: 4},
  {name: 'sql', level: 4},
  {name: 'python', level: 1},
  {name: 'nodeJS', level: 3},
  {name: 'rust', level: 3},
]

interface LevelMeterProps {
  level: number
}
const LevelMeter = ({level}: LevelMeterProps) => {

  return (<div>
    {Array(level).fill(0).map(() => <IoIosStar className={styles.icon}/>)}
  </div>)
}

export const SkillList = () => {

  const [skills, setSkills] = useState(initialState)

  return (<div className={styles.card}>
    <h1>Skills</h1>
    <table>
      <thead>
        <tr className={styles['title']}>
          <th>name</th>
          <th>level</th>
        </tr>
      </thead>
      <tbody>
        {skills.map((skill) => (
          <tr>
            <td className={styles.capitalize}>{skill.name}</td>
            <td><LevelMeter level={skill.level} /></td>
          </tr>
        ))}
      </tbody>
    </table>
  </div>)
}
