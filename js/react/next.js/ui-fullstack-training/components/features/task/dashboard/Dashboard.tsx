'use client'

import { Header } from "@/components/common/Header";
import { SearchBar } from "@/components/common/input/SearchBar";
import { TableHeader, Table } from "@/components/common/table/Table";
import { Task, TaskStatus } from "@/types/Task";
import { PriorityCell } from "./PriorityHeader";
import { TabCustom } from "@/components/common/TabCustom";
import { DragAndDrop } from "@/components/common/DragAndDrop";
import { TaskBoardCard } from "./TaskBoardCard";
import { CustomDialogProps } from "@/components/common/CustomDialog";
import { CreateTaskForm } from "../form/CreateTaskForm";
import { useState } from "react";
import { useRouter } from 'next/navigation';
import { SideBar } from "@/components/common/layout/SideBar";
import { Button } from "@/components/common/Button";
import { Icon } from "@iconify/react";


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
    const todayDate = new Intl.DateTimeFormat('en-US', {
        dateStyle: 'full'
    }).format(Date.now());

    const handleCreateSubmit = async (task: Task) => {
        const createResponse = await fetch('/api/tasks', { method: 'POST', body: JSON.stringify(task) })

        if (task.files) {
            const formData = new FormData()
            task.files.forEach((file) => {
                formData.append("files", file)
            })
            await fetch('/api/tasks/upload', { method: 'POST', body: formData })
        }
        const tasksResponse = await (await fetch('/api/tasks', { method: 'GET' })).json()
        setTasks(tasksResponse)
    }

    const add: CustomDialogProps = {
        label: 'Add',
        icon: 'material-symbols:add',
        content: (setIsOpen) => <CreateTaskForm setIsOpen={setIsOpen} handleSubmit={handleCreateSubmit} />
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
