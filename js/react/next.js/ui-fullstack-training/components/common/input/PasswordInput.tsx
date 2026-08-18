'use client'

import { ChangeEvent, useState } from "react"
import { Label } from "./Label"
import { Icon } from "@iconify/react"
import { ErrorMessage } from "../layout/ErrorMessage"

type PasswordInputProps = {
    label?: string,
    value: any,
    error: string,
    onChange: (value: any) => void
}

export const PasswordInput = ({ label, value, onChange, error }: PasswordInputProps) => {
    const [showPass, setShowPass] = useState(false)

    const handleOnChange = (e: ChangeEvent<HTMLInputElement, HTMLInputElement>) => {
        onChange(e.target.value)
    }

    return (<>
        <Label label={label}
            content={<div className="flex items-center justify-between">
                <input
                    type={showPass ? 'text' : 'password'}
                    className="border-none outline-none"
                    value={value}
                    onChange={(e) => handleOnChange(e)}
                />
                <Icon onClick={() => setShowPass((pass) => !pass)}
                    icon={showPass ? 'mage:eye-off-fill' : 'solar:eye-bold'} />
            </div>
            } />
        <ErrorMessage error={error} />
    </>
    )
}
