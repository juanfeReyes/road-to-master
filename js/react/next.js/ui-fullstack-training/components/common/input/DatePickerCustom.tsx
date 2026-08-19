import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";
import { Label } from "./Label";
import { ErrorMessage } from "../layout/ErrorMessage";

type DatePickerCustomProps = {
    label: string,
    value: Date,
    error?: string,
    onChange: (date: Date) => void
}

export const DatePickerCustom = ({ label, value, onChange, error }: DatePickerCustomProps) => {

    const handleOnChange = (date: Date) => {
        onChange(date)
    }

    return (<>
        <Label label={label}
            content={<DatePicker selected={value} onChange={(date) => handleOnChange(date)} />} />
        <ErrorMessage error={error} />
    </>
    )

}
