import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";
import { Label } from "./Label";
import { FormInputType } from "@/types/FormInputType";

type DatePickerCustomProps = {
    label: string,
    value: Date,
    onChange: (date: Date) => void
} & FormInputType

export const DatePickerCustom = ({ label, value, onChange}: DatePickerCustomProps) => {

    const handleOnChange = (date: Date) => {
        onChange(date)
    }

    return (
        <Label label={label}
            content={<DatePicker selected={value} onChange={(date) => handleOnChange(date)} />} />

    )

}
