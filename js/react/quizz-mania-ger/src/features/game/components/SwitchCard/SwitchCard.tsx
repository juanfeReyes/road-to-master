import React from "react"
import { PropsWithChildren, useState } from "react"

type SwitchCardProps = {
    children?: React.ReactNode[]
}

const SwitchCard = ({ children }: SwitchCardProps) => {
    const [showPrimary, setShowPrimary] = useState(true)
    const primary = React.Children.toArray(children).find(child => child.type == Primary)
    const secondary = React.Children.toArray(children).find(child => child.type == Secondary)

    return (
        <div onClick={() => setShowPrimary((primary) => !primary)}>
            {showPrimary ? primary : secondary}
        </div>
    )
}

const Primary = ({children}) => <div>{children}</div>
const Secondary = ({children}) => <div>{children}</div>

SwitchCard.Primary = Primary
SwitchCard.Secondary = Secondary

export default SwitchCard;
