import { ChangeEvent, Dispatch, SetStateAction } from "react"
import { Label } from "./Label"
import { FormInputType } from "@/types/FormInputType"

type CustomInputProps = {
    label?: string,
} & FormInputType

export const CustomInput = ({ label, form, setForm, inputKey, errors }: CustomInputProps) => {

    const handleOnChange = (e: ChangeEvent<HTMLInputElement, HTMLInputElement>) => {
        setForm({ ...form, [inputKey]: e.target.value })
    }

    return (
        <>
            <Label label={label}
                content={<input className="border-none outline-none"
                    value={form[inputKey]}
                    onChange={(e) => handleOnChange(e)}
                />} />
            {errors?.properties[inputKey] &&
                errors.properties[inputKey].errors.map(e => (<div className="text-red-800">{e}</div>))}
        </>
    )
}
