import { Task, TaskSchema } from '@/types/Task'
import { Collection } from '@msw/data'
import { RecordType } from 'zod/v3'


export const tasksDB = new Collection({
    schema: TaskSchema
})

tasksDB.create({
    id: crypto.randomUUID(),
    name: 'Task 1',
    description: 'hehehehehe',
    dueDate: new Date(),
    priority: 'High',
    status: 'In Progress'
})
tasksDB.create({
    id: crypto.randomUUID(),
    name: 'Task 2',
    description: 'kijuhyyg',
    dueDate: new Date(),
    priority: 'Low',
    status: 'Completed'
})
tasksDB.create({
    id: crypto.randomUUID(),
    name: 'Task 3',
    description: 'awasdfasf',
    dueDate: new Date(),
    priority: 'Medium',
    status: 'Pending'
})
tasksDB.create({
    id: crypto.randomUUID(),
    name: 'Task 4',
    description: 'oijkkjjj',
    dueDate: new Date(),
    priority: 'High',
    status: 'Pending'
})

export const getTasks = () : Task[] => {

    return tasksDB.all().map(record => toPlainObject(record) as Task)
}

const toPlainObject = (record: RecordType<any, any>) => {
    return JSON.parse(JSON.stringify(record))
}

