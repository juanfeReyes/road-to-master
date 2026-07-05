import { Button } from "@/components/common/Button"
import { DatePickerCustom } from "@/components/common/DatePickerCustom"
import { Header } from "@/components/common/Header"
import { Dropdown } from "@/components/common/input/Dropdown"
import { CustomInput } from "@/components/common/input/CustomInput"
import { Dispatch, SetStateAction, useState } from "react"
import { PriorityCell } from "../dashboard/PriorityHeader"
import {TaskPriority, TaskStatus} from '@/types/Task'

import * as z from "zod"
import { Task } from "@/types/Task"

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

const taskSchema = z.object({
    name: z.string(),
    description: z.string(),
    dueDate: z.date(),
    priority: z.string()
})

const formInitialState = {
    name: '',
    description: '',
    dueDate: new Date(),
    priority: priorityOptions[0]
}

export const CreateTaskForm = ({ setIsOpen, handleSubmit }: CreateTaskFormProps) => {

    const [form, setForm] = useState(formInitialState)
    // TODO: Handle errors
    const [errors, setErrors] = useState({})

    const validate = () => {
        taskSchema.safeParse(form)
    }

    const handlesubmit = () => {
        const task = {
            id: crypto.randomUUID(),
            name: form.name,
            description: form.description,
            dueDate: form.dueDate,
            order: 1,
            priority: form.priority.id as TaskPriority,
            status: 'Pending' as TaskStatus
        }
        validate()
        handleSubmit(task)
        setIsOpen(false)
    }

    const handleCancel = () => {
        setForm(formInitialState)
        setIsOpen(false)
    }

    console.log(form)

    return (<div className="flex flex-col gap-3">
        <Header icon="ri:task-fill" label="Create task" />
        <CustomInput label='Name' form={form} setForm={setForm} inputKey="name" />
        <CustomInput label='Description' form={form} setForm={setForm} inputKey="description" />
        <DatePickerCustom label="Due Date" form={form} setForm={setForm} inputKey="dueDate" />
        <Dropdown label="Priorty" options={priorityOptions} form={form} setForm={setForm} inputKey="priority" />
        <div className="flex gap-5 justify-evenly">
            <Button label={'Cancel'} type="Neutral" onClick={handleCancel} />
            <Button label={'Save'} type="Primary" onClick={handlesubmit} />
        </div>
    </div>)
}
