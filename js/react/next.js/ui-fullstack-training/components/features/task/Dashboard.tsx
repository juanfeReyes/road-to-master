'use client'

import { Header } from "@/components/common/Header";
import { SearchBar } from "@/components/common/input/SearchBar";
import { TableHeader, Table } from "@/components/common/table/Table";
import { Task, TaskStatus } from "@/types/Task";
import { PriorityCell } from "./dashboard/PriorityHeader";
import { TabCustom } from "@/components/common/TabCustom";
import { Icon } from "@iconify/react";
import { DragAndDrop } from "@/components/common/DragAndDrop";

const tasks: Task[] = [
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
            name: 'description',
            label: 'Description'
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
    const todayDate = new Intl.DateTimeFormat('en-US', {
        dateStyle: 'full'
    }).format(Date.now());


    
    const tabs = [
        {
            title: <Header icon="cil:list" label="List" />,
            content: () => <Table data={tasks} headers={headers} />
        },
        {
            title: <Header icon="mi:board" label="Board" />,
            content: () => <DragAndDrop
                groups={['Pending', 'In Progress', 'Completed']}
                data={tasks}
                groupBy={(val: Task, group) => val.status === group}
                handleUpdateGroup={(val: Task, group) => { console.log('update {}', group); val.status = group as TaskStatus }}
                card={(val: Task) => <div>{val.name}</div>}
            />
        },
        {
            title: <Header icon="icon-park-solid:pie" label="Graphics" />,
            content: () => <div>Graphics</div>
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
            <div className="h-full">
                <TabCustom tabs={tabs} />
            </div>
        </div>
    )
}
