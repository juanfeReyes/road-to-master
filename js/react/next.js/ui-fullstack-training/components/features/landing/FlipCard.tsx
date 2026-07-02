'use client'

import { ReactNode, useState } from "react"

type FlipCardProps = {
    front: ReactNode,
    back: ReactNode
}
export const FlipCard = ({ front, back }: FlipCardProps) => {

    const [isFlipped, setIsFippled] = useState(false)

    return (<div className="p-2">
        <div className="w-60 h-60 [perspective:1000px] cursor-pointer rounded-2xl" onClick={() => setIsFippled(!isFlipped)}>
            <div className={`bg-stone-50 rounded-2xl relative w-full h-full flex items-center transition-transform duration-700 [transform-style:preserve-3d] ${isFlipped ? '[transform:rotateY(180deg)]' : ''}`}>
                {
                    isFlipped ?
                        <div className="[backface-visibility:hidden] [transform:rotateY(180deg)]">
                            {back}
                        </div> :
                        <div className="[backface-visibility:hidden]">
                            {front}
                        </div>
                }
            </div>
        </div>
    </div>)
}
