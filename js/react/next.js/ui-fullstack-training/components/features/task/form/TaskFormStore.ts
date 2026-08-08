import { Task, TaskStatus } from "@/types/Task";
import { produce } from "immer";
import { create } from "zustand";
import { combine } from "zustand/middleware";

const formInitialState = {
    id: undefined,
    name: '',
    description: '',
    dueDate: new Date(),
    priority: null,
    status: 'Pending' as TaskStatus,
    files: []
}

export const useTaskForm = create(
    combine({ form: formInitialState },
        (set) => ({
            resetForm: () => set((_) => ({ form: formInitialState })),
            loadTask: (task: Task) => set((_) => ({ form: task })),
            updateForm: (property: string, value: any) => set(produce((state) => { state.form[property] = value }))
        })))
