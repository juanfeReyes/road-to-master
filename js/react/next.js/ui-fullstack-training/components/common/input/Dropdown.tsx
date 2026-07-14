'use client'
import { ReactNode, useState } from "react"
import { Label } from "./Label"
import { FormInputType } from "@/types/FormInputType"

type DropdownOption = {
    id: string,
    value: string | ReactNode
}

type DropdownProps = {
    label: string,
    options: DropdownOption[]
} & FormInputType

export const Dropdown = ({ label, options, form, setForm, inputKey }: DropdownProps) => {
    const [isOpen, setIsOpen] = useState(false)

    const toggleDropdown = () => setIsOpen((val) => !val)

    const handleOnClick = (value: DropdownOption) => {
        setForm({ ...form, [inputKey]: value })
        setIsOpen(false)
    }

    return (
        <Label label={label}
            content={<div className="relative">
                <button onClick={toggleDropdown}>
                    {form[inputKey].value}
                </button>
                {isOpen &&
                    <ul className="absolute bg-white rounded-2xl shadow top-full">
                        {options.filter(o => o.id !== form[inputKey]).map(o => (<li
                            className="cursor-pointer"
                            onClick={() => handleOnClick(o)}>{o.value}</li>))}
                    </ul>
                }
            </div>} />
    )
}
