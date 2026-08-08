import { ChangeEvent} from "react"
import { Label } from "./Label"
import { FormInputType } from "@/types/FormInputType"

type CustomInputProps = {
    label?: string,
    value: any,
    onChange: (value: any) => void
} & FormInputType

export const CustomInput = ({ label, inputKey, errors, onChange, value }: CustomInputProps) => {

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
            {errors?.properties[inputKey] &&
                errors.properties[inputKey].errors.map((e, idx) => (<div key={idx} className="text-red-800">{e}</div>))}
        </>
    )
}
