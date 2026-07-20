'use client'

import { Dispatch, ReactNode, SetStateAction, useState } from "react"
import * as motion from "motion/react-client"

type SideBarProps = {
    isBarOpen: boolean
    position: 'right' | 'left'
    mainContent: ReactNode,
    barContent: ReactNode

}

export const SideBar = ({ mainContent, barContent, position, isBarOpen }: SideBarProps) => {

    return (<div className="relative h-full" >
        {mainContent}
        {isBarOpen &&
            <motion.div
                className={`bg-white h-full absolute top-0 ${position}-0`}
                initial={{ x: "100%" }}
                animate={{ x: '50%' }}
                transition={{ type: 'spring', stiffness: 50 }}
            >
                {barContent}
            </motion.div>}
    </div>)
}