import { Listbox, ListboxButton, ListboxOptions, ListboxOption } from "@headlessui/react"
import { useTranslation } from "react-i18next"
import styles from './languageswitcher.module.scss'

export const LanguageSwticher = () => {
  const { i18n, t } = useTranslation()

  return (<div>
    <Listbox value={i18n.language} onChange={i18n.changeLanguage}>
      <ListboxButton>{t(`language.${i18n.language}`)}</ListboxButton>
      <ListboxOptions anchor="bottom end" className={`${styles.popup}`}>
        {Object.keys(i18n.services.resourceStore.data).map((language, idx) => (
          <ListboxOption key={idx} value={language} className="data-focus:bg-blue-100">
            {t(`language.${language}`)}
          </ListboxOption>
        ))}
      </ListboxOptions>
    </Listbox>
  </div>)
}
