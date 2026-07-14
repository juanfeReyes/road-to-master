import { tasksDB } from "@/lib/mocks/TasksDB"

export async function GET(request: Request) {

    const db = tasksDB.all()
    return Response.json(db)
}

export async function POST(request: Request) {
    const task = await request.json()
    const createdTask = await tasksDB.create(task)
    return Response.json(createdTask)
}


