import * as z from "zod"

export type Task = {
    id?: string,
    name: string,
    description: string,
    priority: TaskPriority,
    status?: TaskStatus,
    dueDate: Date,
    files?: File[]
}

const TaskPriority = {
    Critical: 'Critical',
    High: 'High',
    Medium: 'Medium',
    Low: 'Low',
} as const;

export type TaskPriority = typeof TaskPriority[keyof typeof TaskPriority];

const TaskStatus = {
    Pending: 'Pending',
    InProgress: 'In Progress',
    Completed: 'Completed'
} as const;

export type TaskStatus = typeof TaskStatus[keyof typeof TaskStatus];

export const TaskSchema = z.object({
    id: z.uuid().optional(),
    name: z.string().min(3, 'name too short! at least 3 characters'),
    description: z.string().min(5, 'description too short! at least 5 characters'),
    dueDate: z.date().or(z.string()),
    priority: z.custom<TaskPriority>((val) => Object.keys(TaskPriority).includes(val.id ?? val) , 'Invalid Priority'),
    status: z.custom<TaskStatus>().optional(),
    files: z.array(z.custom<File>()).optional(),
})

