import { describe } from "vitest";
import { render } from "vitest-browser-react";
import { TaskDashboard } from "./TaskDashboard";
import { http, HttpResponse } from "msw";
import { test } from "@/lib/test/browser-extend";
import { page } from "vitest/browser";
import { MainProvider } from "@/components/common/layout/mainProvider/mainProvider";

const setup = () => <MainProvider>
    <TaskDashboard />
</MainProvider>

describe('Task Dashboard should', () => {

    test('Render correctly', async ({ worker }) => {
        worker.use(
            http.get('/api/tasks', () => {
                return HttpResponse.json([], {status: 200})
            })
        )
        await render(setup())
    })
})