import { DatePickerCustom } from "@/components/common/input/DatePickerCustom"
import { Header } from "@/components/common/layout/Header"
import { Dropdown, DropdownOption } from "@/components/common/input/Dropdown"
import { Dispatch, SetStateAction, useState } from "react"
import { PriorityCell } from "../dashboard/PriorityHeader"
import { Task, TaskPriority, TaskSchema, TaskStatus } from '@/types/Task'

import * as z from "zod"
import { FormInputErrors } from "@/types/FormInputType"
import { TaskStoreType, useTaskForm } from "./TaskFormStore"
import { CustomInput } from "@/components/common/input/customInput/CustomInput"
import { Button } from "@/components/common/input/button/Button"
import { FileUploader } from "@/components/common/input/FileUploader"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import { useNotification } from "@/components/common/interactivity/useNotification"


type TaskFormProps = {
    setIsOpen: Dispatch<SetStateAction<boolean>>
}


export const TaskForm = ({ setIsOpen }: TaskFormProps) => {
    const form = useTaskForm((state) => state.form)
    const updateForm = useTaskForm((state) => state.updateForm)
    const resetForm = useTaskForm((state) => state.resetForm)
    const [errors, setErrors] = useState<FormInputErrors>()

    const priorityOptions: DropdownOption[] = [
        {
            id: 'Critical',
            value: <PriorityCell value={'Critical'} />,
            onClick: (value: DropdownOption) => { updateForm('priority', value) }
        },
        {
            id: 'High',
            value: <PriorityCell value={'High'} />,
            onClick: (value: DropdownOption) => { updateForm('priority', value) }
        },
        {
            id: 'Medium',
            value: <PriorityCell value={'Medium'} />,
            onClick: (value: DropdownOption) => { updateForm('priority', value) }
        },
        {
            id: 'Low',
            value: <PriorityCell value={'Low'} />,
            onClick: (value: DropdownOption) => { updateForm('priority', value) }
        },
    ]

    const { notify } = useNotification()
    const queryClient = useQueryClient()
    const taskMutation = useMutation({
        mutationFn: (request: TaskStoreType) => {
            if (request.importFile.length > 0) {
                return importTasks(request.importFile)
            }

            const task: Task = {
                id: request.id,
                name: request.name,
                description: request.description,
                dueDate: request.dueDate,
                priority: request.priority.id as TaskPriority,
                status: (request.status ?? 'Pending') as TaskStatus,
                files: request.files
            }
            if (request.id) return updateTask(task)
            return addTask(task)
        },
        onError: (error) => { notify({ value: error.message, type: 'ERROR' }) },
        onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['tasks'] }) }
    })

    const importTasks = async (file: File[]) => {
        const formData = new FormData()
        formData.append("file", file[0])

        const response = await fetch('/api/tasks/bulk', { method: 'POST', body: formData })
        if (!response.ok) throw new Error(`Task import failed - ${(await response.json()).error}`)
        notify({ value: 'Task imported succesfully', type: 'INFO' })
        return response.json()
    }

    const updateTask = async (task: Task) => {
        const formData = new FormData()
        formData.append("task", JSON.stringify(task))
        if (task.files) {
            task.files.forEach((file) => {
                formData.append("files", file)
            })
        }
        const response = await fetch('/api/tasks/' + task.id, { method: 'PUT', body: formData })
        if (!response.ok) throw new Error(`Task creation failed - ${(await response.json()).error}`)
        notify({ value: 'Task updated succesfully', type: 'INFO' })
        return response.json()
    }

    const addTask = async (task: Task) => {
        const formData = new FormData()
        console.log(task)
        formData.append("task", JSON.stringify(task))
        if (task.files) {
            task.files.forEach((file) => {
                formData.append("files", file)
            })
        }
        const response = await fetch('/api/tasks', { method: 'POST', body: formData })
        if (!response.ok) throw new Error(`Task creation failed - ${(await response.json()).error}`)
        notify({ value: 'Task created succesfully', type: 'INFO' })
        return response.json()
    }

    const validate = () => {
        const result = TaskSchema.safeParse(form)
        if (result.error) {
            console.log(result.error)
            const errors = z.treeifyError(result.error)
            setErrors(errors)
        }

        return result;
    }

    const handlesubmit = async () => {
        if (form.importFile) {
            await taskMutation.mutateAsync(form)
            handleCancel()
            return;
        }


        const result = validate()
        if (result.success) {
            await taskMutation.mutateAsync(form)
            handleCancel()
        }
    }

    const handleCancel = () => {
        resetForm()
        setIsOpen(false)
    }

    console.log(form.id)
    return (<div className="flex flex-col gap-3 h-full p-3 min-w-1/2 bg-slate-50 ">
        <Header icon="ri:task-fill" label="Task" />
        {
            !form.id &&
            <FileUploader
                label="Import Task"
                files={form.importFile}
                onChange={(files) => updateForm('importFile', files)}
                errors={errors}
            />
        }
        <CustomInput label='Name' value={form.name} onChange={(value) => updateForm('name', value)} errors={errors} />
        <CustomInput label='Description' value={form.description} onChange={(value) => updateForm('description', value)} errors={errors} />
        <DatePickerCustom label="Due Date" value={form.dueDate} onChange={(date) => updateForm('dueDate', date)} errors={errors} />
        <Dropdown
            id="priority"
            label="Priority"
            buttonLabel={form.priority ? form.priority.value : "Select"}
            options={priorityOptions}
        />
        <FileUploader
            label="Attachments"
            files={form.files}
            onChange={(files) => updateForm('files', files)}
            errors={errors}
        />

        <div className="flex gap-5 justify-evenly">
            <Button label={'Cancel'} type="Neutral" onClick={handleCancel} />
            <Button label={'Save'} type="Primary" onClick={handlesubmit} />
        </div>
    </div>)
}
