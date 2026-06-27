import { useState } from "react";
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";
import { Label } from "./input/Label";

type DatePickerCustomProps = {
    label: string,
}

export const DatePickerCustom = ({ label }: DatePickerCustomProps) => {

    const [startDate, setStartDate] = useState(new Date());
    return (
        <Label label={label}
            content={<DatePicker selected={startDate} onChange={(date) => setStartDate(date)} />} />

    )

}
