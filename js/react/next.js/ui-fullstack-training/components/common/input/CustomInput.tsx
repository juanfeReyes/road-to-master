import { ChangeEvent } from "react"
import { Label } from "./Label"

type CustomInputProps = {
    label?: string,
    value: any,
    error: string,
    onChange: (value: any) => void
}

export const CustomInput = ({ label, onChange, value, error }: CustomInputProps) => {

    const handleOnChange = (e: ChangeEvent<HTMLInputElement, HTMLInputElement>) => {
        onChange(e.target.value)
    }

    return (
        <>
            <Label label={label}
                content={<input className="border-none outline-none"
                    value={value}
                    onChange={(e) => handleOnChange(e)}
                />} />
            {error &&
                <div key={error} className="text-red-800">{error}</div>
            }
        </>
    )
}
