import { MdEngineering } from "react-icons/md"
import { PiUserCircleGear } from "react-icons/pi"
import { NavLink } from "react-router"

import styles from './navbar.module.scss'
import { Menu, MenuButton, MenuItems, MenuItem } from "@headlessui/react"
import { ThemeSwitcher } from "~/components/theme/ThemeSwitcher"
import { LanguageSwticher } from "~/components/i18n/LanguageSwitcher"


export const NavBar = () => {


  return (<div className={`${styles.container}`}>
    <div className={`${styles.home_logo}`}>
      <MdEngineering /> Juan Felipe Reyes
    </div>
    <div className={`${styles.links}`}>
      <NavLink to={'/'}>CV</NavLink>
    </div>
    <div className={`${styles.settings}`}>
      <LanguageSwticher />
      <ThemeSwitcher />
    </div>
  </div>)
}
