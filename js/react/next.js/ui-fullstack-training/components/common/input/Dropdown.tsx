'use client'
import { ReactNode, useState } from "react"
import { Label } from "./Label"

export type DropdownOption = {
    id: string,
    value: string | ReactNode,
    onClick: (value: DropdownOption) => void
}

type DropdownProps = {
    id?: string
    buttonLabel: string | ReactNode
    options: DropdownOption[],
    label?: string,
    className?: string
}

export const Dropdown = ({ id, label, options, buttonLabel, className }: DropdownProps) => {
    const [isOpen, setIsOpen] = useState(false)

    const toggleDropdown = () => setIsOpen((val) => !val)

    const handleOnClick = (value: DropdownOption) => {        
        value.onClick(value)
    }

    return (
        <Label label={label} className={className}
            content={<div className="relative">
                <button onClick={() => toggleDropdown()} data-testid={`dropdown-${id}`}>
                    {buttonLabel}
                </button>
                {isOpen &&
                    <ul className="absolute bg-white rounded-2xl shadow top-full p-2 z-40">
                        {options.filter(o => o.id !== buttonLabel).map((o, idx) => (<li
                            key={idx}
                            className="cursor-pointer"
                            onClick={() => handleOnClick(o)}>{o.value}</li>))}
                    </ul>
                }
            </div>} />
    )
}
