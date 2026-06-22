import { Header } from "@/components/common/Header";
import { SearchBar } from "@/components/common/input/SearchBar";
import { TableHeader, Table } from "@/components/common/table/Table";
import { Task } from "@/types/Task";

export const TaskDashboard = () => {
    const todayDate = new Intl.DateTimeFormat('en-US', {
        dateStyle: 'full'
    }).format(Date.now());

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
            status: 'In Progress'
        },
        {
            id: crypto.randomUUID(),
            name: 'Task 3',
            description: 'awasdfasf',
            dueDate: new Date(),
            order: 1,
            priority: 'Medium',
            status: 'In Progress'
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
            label: 'Priority'
        },
        {
            name: 'status',
            label: 'Status'
        },
    ]

    return (
        <div className="flex flex-col p-2 px-4 gap-6">
            <div className="text-2xl">
                <p className="text-lg">{todayDate}</p>
                <Header icon='material-symbols:task' label="My Tasks" />
            </div>
            <div>
                <SearchBar />
            </div>
            <div>
                <Table data={tasks} headers={headers}/>
            </div>
        </div>
    )
}
