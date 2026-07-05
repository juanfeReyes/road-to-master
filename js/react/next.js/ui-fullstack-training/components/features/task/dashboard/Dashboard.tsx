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

const initialTasks: Task[] = [
    {
        id: crypto.randomUUID(),
        name: 'Task 1',
        description: 'hehehehehe',
        dueDate: new Date(),
        order: 1,
        priority: 'High',
        status: 'In Progress'
    },
    {
        id: crypto.randomUUID(),
        name: 'Task 2',
        description: 'kijuhyyg',
        dueDate: new Date(),
        order: 1,
        priority: 'Low',
        status: 'Completed'
    },
    {
        id: crypto.randomUUID(),
        name: 'Task 3',
        description: 'awasdfasf',
        dueDate: new Date(),
        order: 1,
        priority: 'Medium',
        status: 'Pending'
    }
]

const headers: TableHeader[] = [
        {
            name: 'name',
            label: 'Name'
        },
        {
            name: 'dueDate',
            label: 'Due Date',
            cell: (val: Date) => <>{new Intl.DateTimeFormat('en-US').format(val)}</>
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



export const TaskDashboard = () => {
    const [tasks, setTasks] = useState(initialTasks)

    const todayDate = new Intl.DateTimeFormat('en-US', {
        dateStyle: 'full'
    }).format(Date.now());

    const handleCreateSubmit = (task: Task) => {
        setTasks((tasks) => [...tasks, task])
    }

    const add: CustomDialogProps = {
        label: 'Add',
        icon: 'material-symbols:add',
        content: (setIsOpen) => <CreateTaskForm setIsOpen={setIsOpen} handleSubmit={handleCreateSubmit}/>
    }
    
    const tabs = [
        {
            title: <Header icon="cil:list" label="List" />,
            content: () => <Table data={tasks} headers={headers} add={add}/>
        },
        {
            title: <Header icon="mi:board" label="Board" />,
            content: () => <DragAndDrop
                groups={['Pending', 'In Progress', 'Completed']}
                data={tasks}
                add={add}
                groupBy={(val: Task, group) => val.status === group}
                handleUpdateGroup={(val: Task, group) => { console.log('update {}', group); val.status = group as TaskStatus }}
                card={(val: Task) => <TaskBoardCard task={val} />}
            />
        }
    ]

    return (
        <div className="flex flex-col p-2 px-4 gap-6 bg-sky-100 h-full">
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
        </div>
    )
}
