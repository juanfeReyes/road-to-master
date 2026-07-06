import { Button } from "@/components/common/Button"
import { DatePickerCustom } from "@/components/common/DatePickerCustom"
import { Header } from "@/components/common/Header"
import { Dropdown } from "@/components/common/input/Dropdown"
import { CustomInput } from "@/components/common/input/CustomInput"
import { Dispatch, SetStateAction, useState } from "react"
import { PriorityCell } from "../dashboard/PriorityHeader"
import { TaskPriority, TaskStatus } from '@/types/Task'

import * as z from "zod"
import { Task } from "@/types/Task"
import { FormInputErrors } from "@/types/FormInputType"

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
    name: z.string().min(3, 'name too short! at least 3 characters'),
    description: z.string().min(5, 'description too short! at least 5 characters'),
    dueDate: z.date(),
    priority: z.any() // TODO: improve schema to validate the options
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
    const [errors, setErrors] = useState<FormInputErrors>()

    const validate = () => {
        const result = taskSchema.safeParse(form)
        if (result.error) {
            const errors = z.treeifyError(result.error)
            setErrors(errors)
        }

        return result;
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

    console.log(errors)

    return (<div className="flex flex-col gap-3">
        <Header icon="ri:task-fill" label="Create task" />
        <CustomInput label='Name' form={form} setForm={setForm} inputKey="name" errors={errors}/>
        <CustomInput label='Description' form={form} setForm={setForm} inputKey="description" errors={errors}/>
        <DatePickerCustom label="Due Date" form={form} setForm={setForm} inputKey="dueDate"errors={errors} />
        <Dropdown label="Priorty" options={priorityOptions} form={form} setForm={setForm} inputKey="priority"  errors={errors}/>
        <div className="flex gap-5 justify-evenly">
            <Button label={'Cancel'} type="Neutral" onClick={handleCancel} />
            <Button label={'Save'} type="Primary" onClick={handlesubmit} />
        </div>
    </div>)
}
