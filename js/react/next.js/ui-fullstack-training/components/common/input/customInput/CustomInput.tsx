import { ChangeEvent } from "react"
import { Label } from "../Label"
import { ErrorMessage } from "../../layout/ErrorMessage"

type CustomInputProps = {
    label?: string,
    value: any,
    error?: string,
    placeholder?: string,
    onChange: (value: any) => void
}

export const CustomInput = ({ label, onChange, value, error, placeholder }: CustomInputProps) => {

    const handleOnChange = (e: ChangeEvent<HTMLInputElement, HTMLInputElement>) => {
        onChange(e.target.value)
    }

    return (
        <>
            <Label label={label}
                content={<input className="border-none outline-none"
                    value={value}
                    onChange={(e) => handleOnChange(e)}
                    placeholder={placeholder}
                />} />
            <ErrorMessage error={error} />
        </>
    )
}
