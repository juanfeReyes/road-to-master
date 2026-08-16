'use client'

import { Task } from "@/types/Task";
import { Suspense, useState } from "react";
import { SideBar } from "@/components/common/layout/SideBar";
import { useNotification } from "@/components/common/interactivity/useNotification";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { ErrorBoundary } from "react-error-boundary";
import { ErrorFallback } from "@/components/common/interactivity/ErrorFallback";
import { TaskDetail } from "./taskDetail/TaskDetail";
import { TaskDetailSkeleton } from "./taskDetail/TaskDetailSkeleton";
import { TaskForm } from "../form/TaskForm";


type TaskDashboardProps = {
}

export const TaskDashboard = ({ }: TaskDashboardProps) => {

    const [isBarOpen, setIsBarOpen] = useState(false)
    const { notify } = useNotification()
    const queryClient = useQueryClient()
    const taskMutation = useMutation({
        mutationFn: (task: Task) => { return task.id ? updateTask(task) : addTask(task) },
        onError: (error) => { notify({ value: error.message, type: 'ERROR' }) },
        onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['tasks'] }) }
    })

    const updateTask = async (task: Task) => {
        const formData = new FormData()
        formData.append("task", JSON.stringify(task))
        if (task.files) {
            task.files.forEach((file) => {
                formData.append("files", file)
            })
        }
        const response = await fetch('/api/tasks/'+task.id, { method: 'PUT', body: formData })
        if (!response.ok) throw new Error(`Task creation failed - ${(await response.json()).error}`)
        notify({ value: 'Task updated succesfully', type: 'INFO' })
        return response.json()
    }

    const addTask = async (task: Task) => {
        const formData = new FormData()
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

    const handleSubmit = async (task: Task) => {
        console.log('Handle submit')
        await taskMutation.mutateAsync(task)
    }

    return (
        <ErrorBoundary
            fallbackRender={(props) => (<ErrorFallback {...props} businessMessage={"Failed to load Tasks"} />)}
        >
            <Suspense key={"task-details"} fallback={<TaskDetailSkeleton />}>
                <div className="flex flex-col p-2 px-4 gap-6 bg-sky-100 h-full">
                    <SideBar
                        isBarOpen={isBarOpen}
                        position="right"
                        mainContent={<TaskDetail setIsBarOpen={setIsBarOpen} />}
                        barContent={<TaskForm setIsOpen={setIsBarOpen} handleSubmit={handleSubmit} />}
                    />
                </div>
            </Suspense>
        </ErrorBoundary>
    )
}
