import { Icon } from "@iconify/react"

const buttonTypes = ["Info", "Error", "Neutro"] as const;
type ButtonType = typeof buttonTypes[number]

type ButtonProps = {
    type: ButtonType,
    label: string,
    icon?: string,
    onClick: () => void
}

const buttonColor = {
    "Info": "bg-blue-300",
    "Error": "bg-red-400",
    "Neutro": "bg-gray-400",
}

export const Button = ({ label, icon, onClick, type }: ButtonProps) => {

    return <>
        <button className={`${buttonColor[type]} rounded-xl p-1`} onClick={onClick}>
            {icon && <Icon icon={icon} />}
            {label}
        </button>
    </>
}
