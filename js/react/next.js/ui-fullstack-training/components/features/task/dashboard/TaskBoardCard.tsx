import { Task } from "@/types/Task"
import { PriorityCell } from "./PriorityHeader"

type TaskBoardCardProps = {
    task: Task
}

export const TaskBoardCard = ({ task }: TaskBoardCardProps) => {

    return (<div className="bg-white shadow-lg rounded-lg p-4 flex flex-col gap-4 w-50">
        <div>
            <h1>{task.name}</h1>
            <p className="text-sm font-medium text-right">{new Intl.DateTimeFormat('en-US').format(Date.parse(task.dueDate))}</p>
        </div>
        <div className="">
            <PriorityCell value={task.priority} />
        </div>
    </div>)
}
