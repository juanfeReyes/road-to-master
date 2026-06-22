import { Task } from "./Task"

export type Project = {
    id: string,
    name: string,
    tasks: Task[],
    status: ProjectStatus
}

const ProjectStatus = {
    Open: 'Open',
    Closed: 'Closed'
} as const;

export type ProjectStatus = typeof ProjectStatus[keyof typeof ProjectStatus]; 