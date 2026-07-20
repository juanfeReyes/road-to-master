import { Icon } from "@iconify/react"

type HeaderProps = {
    icon: string,
    label: string
}

export const Header = ({icon, label}: HeaderProps) => {

    return (
        <div id={label} className="flex gap-2 items-center">
            <Icon icon={icon}/>
            <p>{label}</p>
        </div>
    )
}
