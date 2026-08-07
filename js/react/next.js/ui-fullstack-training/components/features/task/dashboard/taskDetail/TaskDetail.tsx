import { Button } from "@/components/common/input/Button";
import { SearchBar } from "@/components/common/input/SearchBar";
import { Header } from "@/components/common/layout/Header";
import { TabCustom } from "@/components/common/layout/TabCustom";
import { Table, TableHeader } from "@/components/common/table/Table";
import { Task } from "@/types/Task";
import { Icon } from "@iconify/react";
import { useSuspenseQuery } from "@tanstack/react-query";
import { Dispatch, SetStateAction } from "react";
import { PriorityCell } from "../PriorityHeader";

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
    }
]

type TaskDetailProps = {
    setIsBarOpen: Dispatch<SetStateAction<boolean>>
}

export const TaskDetail = ({setIsBarOpen}: TaskDetailProps) => {
    const todayDate = new Intl.DateTimeFormat('en-US', {
        dateStyle: 'full'
    }).format(Date.now());
    
    const getTasks = async (): Promise<Task[]> => {
        return await (await fetch('/api/tasks', { method: 'GET' })).json()
    }

    const { data, isLoading } = useSuspenseQuery({
        queryKey: ['tasks'],
        queryFn: getTasks
    })

    const tabs = [
        {
            title: <Header icon="cil:list" label="Table" />,
            content: () => <Table isPending={isLoading} data={data} headers={headers}
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
        <div className="flex flex-col gap-3">
            <div className="text-2xl flex flex-col gap-4">
                <p className="text-lg">{todayDate}</p>
                <Header icon='material-symbols:task' label="My Tasks" />
            </div>
            <div className="h-full overflow-auto">
                <TabCustom tabs={tabs} />
            </div>
        </div>
    )
}
