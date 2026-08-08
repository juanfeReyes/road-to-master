import { Button } from "@/components/common/input/Button";
import { Header } from "@/components/common/layout/Header";
import { TabCustom } from "@/components/common/layout/TabCustom";
import { Table, TableHeader } from "@/components/common/table/Table";
import { Task } from "@/types/Task";
import { Icon } from "@iconify/react";
import { useMutation, useQueryClient, useSuspenseQuery } from "@tanstack/react-query";
import { Dispatch, SetStateAction } from "react";
import { PriorityCell } from "../PriorityHeader";
import { Dropdown, DropdownOption } from "@/components/common/input/Dropdown";
import { useTaskForm } from "../../form/TaskFormStore";
import { useNotification } from "@/components/common/interactivity/useNotification";

const buildRowOptions = (handleUpdate, handleDelete) => {
    return [
        {
            id: 'update',
            value: 'Update',
            onClick: (value: DropdownOption) => { handleUpdate() }
        },
        {
            id: 'delete',
            value: 'Delete',
            onClick: (value: DropdownOption) => { handleDelete() }
        }
    ]
}

const buildHeaders = (handleUpdate, handleDelete) => {
    return [
        {
            name: 'name',
            label: 'Name',
            cell: (val: string, row) => <div className="flex gap-2 justify-between w-full">
                <p>{val}</p>
                <Dropdown
                    className=""
                    buttonLabel={<Icon icon={'mage:dots'} />}
                    options={buildRowOptions(() => handleUpdate(row), () => handleDelete(row))}
                />
            </div>
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
}

type TaskDetailProps = {
    setIsBarOpen: Dispatch<SetStateAction<boolean>>
}

export const TaskDetail = ({ setIsBarOpen }: TaskDetailProps) => {
    const loadTask = useTaskForm((state) => state.loadTask)
    const queryClient = useQueryClient()
    const { notify } = useNotification()
    const taskMutation = useMutation({
        mutationFn: (task: Task) => {return fetch('/api/tasks/'+task.id, { method: 'DELETE' })},
        onError: (error) => { notify({ value: error.message, type: 'ERROR' }) },
        onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['tasks'] }) }
    })
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

    const handleUpdate = (task: Task) => {
        console.log(task)
        loadTask(task)
        setIsBarOpen(true)
    }

    const handleDelete = async (task: Task) => {
        await taskMutation.mutateAsync(task)
    }

    const tabs = [
        {
            title: <Header icon="cil:list" label="Table" />,
            content: () => <Table isPending={isLoading} data={data} headers={buildHeaders(handleUpdate, handleDelete)}
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
        <div className="flex flex-col gap-3 h-full">
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
