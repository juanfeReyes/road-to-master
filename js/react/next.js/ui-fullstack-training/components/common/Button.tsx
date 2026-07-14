import { ReactNode } from "react"

const buttonType = {
    Primary: 'Primary',
    Secondary: 'Secondary',
    Neutral: 'Neutral'
} as const;

type ButtonType = typeof buttonType[keyof typeof buttonType]

type ButtonProps = {
    onClick: () => void,
    label: string | ReactNode
    type: ButtonType
}

const style = {
    Primary: '',
    Secondary: '',
    Neutral: ''
}

export const Button = ({label, type, onClick}: ButtonProps) => {
    return (<button className={`${style[type]}`} onClick={onClick} >
        {label}
    </button>)
}
