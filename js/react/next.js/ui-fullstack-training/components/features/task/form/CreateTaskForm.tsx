import { Button } from "@/components/common/Button"
import { DatePickerCustom } from "@/components/common/DatePickerCustom"
import { Header } from "@/components/common/Header"
import { CustomInput } from "@/components/common/input/Input"
import { Dispatch, SetStateAction } from "react"

type CreateTaskFormProps = {
    setIsOpen:  Dispatch<SetStateAction<boolean>>
}

export const CreateTaskForm = ({setIsOpen}: CreateTaskFormProps) => {

    return (<div className="flex flex-col gap-3">
        <Header icon="ri:task-fill" label="Create task"/>
        <CustomInput label='Name' />
        <CustomInput label='Description' />
        <DatePickerCustom label="Due Date"/>
        <div className="flex gap-5 justify-evenly">
            <Button label={'Cancel'} type="Neutral" onClick={() => {setIsOpen(false)}} />
            <Button label={'Save'} type="Primary" onClick={() => {setIsOpen(false)}} />
        </div>
    </div>)
}
