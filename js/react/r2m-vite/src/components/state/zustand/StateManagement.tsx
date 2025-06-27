import { FaRegStar, FaStar } from "react-icons/fa"
import { useSkills, useSkillsActions } from "./SkillState"
import { IoIosAddCircleOutline } from "react-icons/io"
import { useState, type ChangeEvent } from "react"


interface SkillProps {
  name: string,
  level: number
}

const Skill = ({ name, level }: SkillProps) => {

  return (<div>
    <div>{name}</div>
    {Array(level).fill(0).map(() => <FaStar />)}
    {Array(5 - level).fill(0).map(() => <FaRegStar />)}
  </div>)
}

export const StateManagement = () => {
  const [skill, setSkill] = useState({name: '', level: ''})
  const skills = useSkills()
  const { addSkill, removeSkill } = useSkillsActions()
  console.log(skills)

  const handleSkillChange = (event: ChangeEvent<HTMLInputElement>) => {
    setSkill({...skill, [event.target.name]: event.target.value})
  }

  const handleAddSkill = () => {
    addSkill({name: skill.name, level: Number(skill.level)})
  }

  return (<div>
    <h1>Skills</h1>
    <div>
      <label htmlFor="name">name</label>
      <input name="name" value={skill.name} onChange={handleSkillChange}/>
      <label htmlFor="level">level</label>
      <input name="level" value={skill.level} onChange={handleSkillChange}/>
      <button onClick={handleAddSkill}><IoIosAddCircleOutline />Add skill</button>
    </div>
    {Object.keys(skills).map((name) => <Skill name={name} level={skills[name]} />)}

  </div>)
}
