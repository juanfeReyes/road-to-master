import { ChangeEvent, useState } from "react";
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";
import { Label } from "./Label";
import { FormInputType } from "@/types/FormInputType";

type DatePickerCustomProps = {
    label: string,
} & FormInputType

export const DatePickerCustom = ({ label, form, setForm, inputKey }: DatePickerCustomProps) => {

    const handleOnChange = (date: Date) => {
        setForm({ ...form, [inputKey]: date })
    }

    return (
        <Label label={label}
            content={<DatePicker selected={form[inputKey]} onChange={(date) => handleOnChange(date)} />} />

    )

}
