import { create } from "zustand";

interface Skills {
  [name: string]: number
}

interface SkillState {
  skills: Skills
  actions: {
    addSkill: (skill: {name: string, level: number}) => void
    removeSkill: (name: string) => void
  }
}

const useSkillStore = create<SkillState>((set, get) => ({
  skills: {},
  actions: {
    addSkill: (skill: {name: string, level: number}) => set((state) => ({ skills: {...state.skills, [skill.name]: skill.level}  })),
    removeSkill: (name: string) => {
      const temp = { ...get().skills }
      delete temp[name]
      set({ skills: temp })
    }
  }
}))

export const useSkills = () => useSkillStore((state) => state.skills)
export const useSkillsActions = () => useSkillStore((state) => state.actions)
