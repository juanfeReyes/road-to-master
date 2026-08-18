import { ComponentPropsWithoutRef, ReactNode } from "react"
import { PartialComponent } from "../../PartialComponent";

type ButtonProps = ComponentPropsWithoutRef<'div'> & {
    onClick: () => void,
    label: string | ReactNode
}

const Button = ({ label, onClick, className }: ButtonProps) => {
    return (<button
        type="button"
        className={`${className} px-2 rounded-lg text-white `}
        onClick={onClick} >
        {label}
    </button>)
}

export const PrimaryButton = PartialComponent(Button, { className: 'bg-blue-500 font-bold' })
export const SecondaryButton = PartialComponent(Button, { className: 'bg-indigo-500' })
export const NeutralButton = PartialComponent(Button, { className: ' bg-slate-500' })
