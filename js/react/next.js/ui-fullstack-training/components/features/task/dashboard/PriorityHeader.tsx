
import {TaskPriority} from '@/types/Task'

const styleConfig: Record<TaskPriority, string> = {
    'Critical': 'bg-orange-700 text-slate-50',
    'High': 'bg-red-500',
    'Low': 'bg-emerald-300',
    'Medium': 'bg-yellow-300'
}

type PriorityHeaderProps = {
    value: TaskPriority
}

export const PriorityCell = ({value}: PriorityHeaderProps) => {

    return (<div className={`${styleConfig[value]} bg rounded-2xl m-1 mx-3 text-center px-5`}>
        {value}
    </div>)
}
