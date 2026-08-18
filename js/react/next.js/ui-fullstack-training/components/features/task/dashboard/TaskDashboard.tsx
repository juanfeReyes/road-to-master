'use client'

import { Suspense, useState } from "react";
import { SideBar } from "@/components/common/layout/SideBar";
import { ErrorBoundary } from "react-error-boundary";
import { ErrorFallback } from "@/components/common/interactivity/ErrorFallback";
import { TaskDetail } from "./taskDetail/TaskDetail";
import { TaskDetailSkeleton } from "./taskDetail/TaskDetailSkeleton";
import { TaskForm } from "../form/TaskForm";


type TaskDashboardProps = {
}

export const TaskDashboard = ({ }: TaskDashboardProps) => {

    const [isBarOpen, setIsBarOpen] = useState(false)

    return (
        <ErrorBoundary
            fallbackRender={(props) => (<ErrorFallback {...props} businessMessage={"Failed to load Tasks"} />)}
        >
            <Suspense key={"task-details"} fallback={<TaskDetailSkeleton />}>
                <div className="flex flex-col p-2 px-4 gap-6 bg-linear-to-b from-blue-200 via-blue-300 to-indigo-600 h-full">
                    <SideBar
                        isBarOpen={isBarOpen}
                        position="right"
                        mainContent={<TaskDetail setIsBarOpen={setIsBarOpen} />}
                        barContent={<TaskForm setIsOpen={setIsBarOpen}/>}
                    />
                </div>
            </Suspense>
        </ErrorBoundary>
    )
}
