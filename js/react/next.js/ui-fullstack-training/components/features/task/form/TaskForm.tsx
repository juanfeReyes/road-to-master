import { DatePickerCustom } from "@/components/common/input/DatePickerCustom"
import { Header } from "@/components/common/layout/Header"
import { Dropdown, DropdownOption } from "@/components/common/input/Dropdown"
import { Dispatch, SetStateAction, useState } from "react"
import { PriorityCell } from "../dashboard/PriorityHeader"
import { Task, TaskPriority, TaskSchema, TaskStatus } from '@/types/Task'

import * as z from "zod"
import { FormInputErrors } from "@/types/FormInputType"
import { FileUploader } from "@/components/common/input/FileUploader"
import { useTaskForm } from "./TaskFormStore"
import { CustomInput } from "@/components/common/input/customInput/CustomInput"
import { Button } from "@/components/common/input/button/Button"


type TaskFormProps = {
    setIsOpen: Dispatch<SetStateAction<boolean>>
    handleSubmit: (task: Task) => void
}


export const TaskForm = ({ setIsOpen, handleSubmit }: TaskFormProps) => {
    const form = useTaskForm((state) => state.form)
    const updateForm = useTaskForm((state) => state.updateForm)
    const resetForm = useTaskForm((state) => state.resetForm)
    const [errors, setErrors] = useState<FormInputErrors>()

    const priorityOptions: DropdownOption[] = [
        {
            id: 'Critical',
            value: <PriorityCell value={'Critical'} />,
            onClick: (value: DropdownOption) => { updateForm('priority', value ) }
        },
        {
            id: 'High',
            value: <PriorityCell value={'High'} />,
            onClick: (value: DropdownOption) => { updateForm('priority', value ) }
        },
        {
            id: 'Medium',
            value: <PriorityCell value={'Medium'} />,
            onClick: (value: DropdownOption) => { updateForm('priority', value ) }
        },
        {
            id: 'Low',
            value: <PriorityCell value={'Low'} />,
            onClick: (value: DropdownOption) => { updateForm('priority', value ) }
        },

    ]

    const validate = () => {
        const result = TaskSchema.safeParse(form)
        if (result.error) {
            console.log(result.error)
            const errors = z.treeifyError(result.error)
            setErrors(errors)
        }

        return result;
    }

    const handlesubmit = () => {
        const task: Task = {
            id: form.id,
            name: form.name,
            description: form.description,
            dueDate: form.dueDate,
            priority: form.priority.id as TaskPriority,
            status: (form.status ?? 'Pending') as TaskStatus,
            files: form.files
        }
        const result = validate()
        if (result.success) {
            handleSubmit(task)
            setIsOpen(false)
            resetForm()
        }
    }

    const handleCancel = () => {
        resetForm()
        setIsOpen(false)
    }

    return (<div className="flex flex-col gap-3 h-full p-3 min-w-1/2 bg-slate-50 ">
        <Header icon="ri:task-fill" label="Task" />
        <CustomInput label='Name' value={form.name} onChange={(value) => updateForm('name', value)}  errors={errors} />
        <CustomInput label='Description' value={form.description} onChange={(value) => updateForm('description', value)} errors={errors} />
        <DatePickerCustom label="Due Date" value={form.dueDate} onChange={(date) => updateForm('dueDate', date)} errors={errors} />
        <Dropdown
            id="priority"
            label="Priority"
            buttonLabel={form.priority ? form.priority.value : "Select"}
            options={priorityOptions}
        />
        <FileUploader files={form.files} onChange={(files) => updateForm('files', files)} errors={errors} />
        <div className="flex gap-5 justify-evenly">
            <Button label={'Save'} type="Primary" onClick={handlesubmit} />
            <Button label={'Cancel'} type="Neutral" onClick={handleCancel} />
        </div>
    </div>)
}
