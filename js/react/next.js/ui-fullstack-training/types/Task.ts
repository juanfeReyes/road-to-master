export type Task = {
    id: string,
    name: string,
    description: string,
    priority: TaskPriority,
    status: TaskStatus,
    dueDate: Date,
    order: number
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
