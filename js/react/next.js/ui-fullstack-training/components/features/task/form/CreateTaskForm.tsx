import { Button } from "@/components/common/input/Button"
import { DatePickerCustom } from "@/components/common/input/DatePickerCustom"
import { Header } from "@/components/common/layout/Header"
import { Dropdown } from "@/components/common/input/Dropdown"
import { CustomInput } from "@/components/common/input/CustomInput"
import { Dispatch, SetStateAction, useState } from "react"
import { PriorityCell } from "../dashboard/PriorityHeader"
import { TaskPriority, TaskSchema, TaskStatus } from '@/types/Task'

import * as z from "zod"
import { Task } from "@/types/Task"
import { FormInputErrors } from "@/types/FormInputType"
import { FileUploader } from "@/components/common/input/FileUploader"

const priorityOptions = [
    {
        id: 'Critical',
        value: <PriorityCell value={'Critical'} />
    },
    {
        id: 'High',
        value: <PriorityCell value={'High'} />
    },
    {
        id: 'Medium',
        value: <PriorityCell value={'Medium'} />
    },
    {
        id: 'Low',
        value: <PriorityCell value={'Low'} />
    },

]

type CreateTaskFormProps = {
    setIsOpen: Dispatch<SetStateAction<boolean>>
    handleSubmit: (task: Task) => void
}



const formInitialState = {
    name: '',
    description: '',
    dueDate: new Date(),
    priority: priorityOptions[0],
    files: []
}

export const CreateTaskForm = ({ setIsOpen, handleSubmit }: CreateTaskFormProps) => {

    const [form, setForm] = useState(formInitialState)
    const [errors, setErrors] = useState<FormInputErrors>()

    const validate = () => {
        const result = TaskSchema.safeParse(form)
        if (result.error) {
            const errors = z.treeifyError(result.error)
            setErrors(errors)
        }

        return result;
    }

    const handlesubmit = () => {
        const task: Task = {
            id: crypto.randomUUID(),
            name: form.name,
            description: form.description,
            dueDate: form.dueDate,
            priority: form.priority.id as TaskPriority,
            status: 'Pending' as TaskStatus,
            files: form.files
        }
        const result = validate()
        if(result.success) {
            handleSubmit(task)
            setIsOpen(false)
        }
    }

    const handleCancel = () => {
        setForm(formInitialState)
        setIsOpen(false)
    }


    return (<div className="flex flex-col gap-3 h-full p-3 min-w-1/2">
        <Header icon="ri:task-fill" label="Create task" />
        <CustomInput label='Name' form={form} setForm={setForm} inputKey="name" errors={errors}/>
        <CustomInput label='Description' form={form} setForm={setForm} inputKey="description" errors={errors}/>
        <DatePickerCustom label="Due Date" form={form} setForm={setForm} inputKey="dueDate" errors={errors} />
        <Dropdown label="Priorty" options={priorityOptions} form={form} setForm={setForm} inputKey="priority"  errors={errors}/>
        <FileUploader form={form} setForm={setForm} inputKey="files" errors={errors} />
        <div className="flex gap-5 justify-evenly">
            <Button label={'Cancel'} type="Neutral" onClick={handleCancel} />
            <Button label={'Save'} type="Primary" onClick={handlesubmit} />
        </div>
    </div>)
}
