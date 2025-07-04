import { create } from "zustand";

interface i18nStore {
  lng: string,
  actions: {
    setLanguage: (lng: string) => void
  }
}

const useI18nStore = create<i18nStore>((set, get) => ({
  lng: 'es',
  actions: {
    setLanguage: (lng: string) => set(() => ({lng: lng}))
  }
}))

export const usei18nLanguage = () => useI18nStore((state) => state.lng)
export const usei18nActions = () => useI18nStore((state) => state.actions)
