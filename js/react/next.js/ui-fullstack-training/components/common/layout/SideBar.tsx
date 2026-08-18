'use client'

import { ReactNode } from "react"
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
            <div className="absolute top-0 right-0 h-dvh">
                <motion.div
                    layout
                    style={{
                        position: "absolute",
                        top: 0,
                        right: 0,
                        height: '95dvh',
                        width: '40dvh'
                    }}
                    initial={{ opacity: 0, y: -20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ duration: 0.5 }}
                    exit={{ opacity: 0, y: -20 }}
                >
                    {barContent}
                </motion.div>
            </div>
        }
    </div>)
}