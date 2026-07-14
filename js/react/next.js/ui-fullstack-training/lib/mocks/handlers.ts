import { Task } from '@/types/Task'
import {http, HttpResponse} from 'msw'
import { tasksDB } from './TasksDB'

export const handlers = [
    http.get('https://api.r2m.com/tasks', () => {
        return HttpResponse.json(tasksDB.all())
    }),
    http.post('https://api.r2m.com/tasks', async ({request}) => {
        const task = request.json();
        console.log('task requested ->', task)
        const result = await tasksDB.create(task)
        return HttpResponse.json(result, {status: 201})
    })
]
