'use client'

import { Header } from "@/components/common/layout/Header";
import { SearchBar } from "@/components/common/input/SearchBar";
import { TableHeader, Table } from "@/components/common/table/Table";
import { Task } from "@/types/Task";
import { PriorityCell } from "./PriorityHeader";
import { TabCustom } from "@/components/common/layout/TabCustom";
import { CreateTaskForm } from "../form/CreateTaskForm";
import { useState } from "react";
import { SideBar } from "@/components/common/layout/SideBar";
import { Button } from "@/components/common/input/Button";
import { Icon } from "@iconify/react";
import { useNotification } from "@/components/common/interactivity/useNotification";
import { useMutation } from "@tanstack/react-query";


const headers: TableHeader[] = [
    {
        name: 'name',
        label: 'Name'
    },
    {
        name: 'dueDate',
        label: 'Due Date',
        cell: (val: string) => <>{new Intl.DateTimeFormat('en-US').format(Date.parse(val))}</>
    },
    {
        name: 'priority',
        label: 'Priority',
        cell: (val) => <PriorityCell value={val} />
    },
    {
        name: 'status',
        label: 'Status'
    },
]

type TaskDashboardProps = {
    initialData: Task[]
}

export const TaskDashboard = ({ initialData }: TaskDashboardProps) => {
    const [tasks, setTasks] = useState(initialData)
    const [isBarOpen, setIsBarOpen] = useState(false)
    const { notify } = useNotification()
    const taskMutation = useMutation({
        mutationFn: (task: Task) => { return addTask(task) },
        onError: (error) => { notify({ value: error.message, type: 'ERROR' }) }
    })

    const todayDate = new Intl.DateTimeFormat('en-US', {
        dateStyle: 'full'
    }).format(Date.now());

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
        return response.json()
    }

    const handleCreateSubmit = async (task: Task) => {
        await taskMutation.mutate(task)
        const tasksResponse = await (await fetch('/api/tasks', { method: 'GET' })).json()
        setTasks(tasksResponse)
        notify({ value: 'Task created succesfully', type: 'INFO' })
    }

    const tabs = [
        {
            title: <Header icon="cil:list" label="Table" />,
            content: () => <Table data={tasks} headers={headers}
                headerContent={<Button label={<p className="text-xl flex gap-1 items-center"><Icon icon={'basil:add-outline'} />Add</p>}
                    type='Primary'
                    onClick={() => setIsBarOpen(true)} />} />
        },
        // IN-PROGRESS
        // {
        //     title: <Header icon="mi:board" label="Board" />,
        //     content: () => <DragAndDrop
        //         groups={['Pending', 'In Progress', 'Completed']}
        //         data={tasks}
        //         add={add}
        //         groupBy={(val: Task, group) => val.status === group}
        //         handleUpdateGroup={(val: Task, group) => { console.log('update {}', group); val.status = group as TaskStatus }}
        //         card={(val: Task) => <TaskBoardCard task={val} />}
        //     />
        // }
    ]

    return (
        <div className="flex flex-col p-2 px-4 gap-6 bg-sky-100 h-full">
            <SideBar
                isBarOpen={isBarOpen}
                position="right"
                mainContent={
                    <>
                        <div className="text-2xl flex flex-col gap-4">
                            <p className="text-lg">{todayDate}</p>
                            <Header icon='material-symbols:task' label="My Tasks" />
                        </div>
                        <div>
                            <SearchBar />
                        </div>
                        <div className="h-full overflow-auto">
                            <TabCustom tabs={tabs} />
                        </div>
                    </>
                }
                barContent={<CreateTaskForm setIsOpen={setIsBarOpen} handleSubmit={handleCreateSubmit} />}
            />

        </div>
    )
}
