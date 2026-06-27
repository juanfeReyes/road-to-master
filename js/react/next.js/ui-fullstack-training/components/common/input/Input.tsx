import { Label } from "./Label"

type CustomInputProps = {
    label?: string
}

export const CustomInput = ({ label }: CustomInputProps) => {

    return (
        <Label label={label}
            content={<input className="border-none outline-none" />} />
    )
}
