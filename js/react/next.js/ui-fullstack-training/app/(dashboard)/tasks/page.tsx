import { TaskDashboard } from "@/components/features/task/dashboard/Dashboard";
import { getTasks, tasksDB } from "@/lib/mocks/TasksDB";
import { Task } from "@/types/Task";

export default async function TaskPage () {
    const tasks = await getTasks()
    return (
        <TaskDashboard initialData={tasks} />
    )
}
