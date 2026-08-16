import { describe, expect, vi } from "vitest";
import { render } from "vitest-browser-react";
import { TaskDashboard } from "./TaskDashboard";
import { http, HttpResponse } from "msw";
import { test } from "@/lib/test/browser-extend";
import { page } from "vitest/browser";
import { MainProvider } from "@/components/common/layout/mainProvider/mainProvider";
import { Task } from "@/types/Task";

const defaultTasks = [
    {
        id: crypto.randomUUID(),
        name: 'Test 1',
        description: 'Test 1 description',
        dueDate: new Date(),
        status: 'In Progress',
        priority: 'High',
        files: []
    },
    {
        id: crypto.randomUUID(),
        name: 'Test 2',
        description: 'Test 2 description',
        dueDate: new Date(),
        status: 'Completed',
        priority: 'Low',
        files: []
    },
    {
        id: crypto.randomUUID(),
        name: 'Test 3',
        description: 'Test 3 description',
        dueDate: new Date(),
        status: 'Pending',
        priority: 'Medium',
        files: []
    }
] as Task[]

const setup = () => <MainProvider>
    <TaskDashboard />
</MainProvider>

describe('Task Dashboard should', () => {

    test('Render correctly', async ({ worker }) => {
        worker.use(
            http.get('/api/tasks', () => {
                return HttpResponse.json([], { status: 200 })
            }, { once: true })
        )
        await render(setup())

        const noDataProvided = page.getByRole('tabpanel', { hasText: /No data provided/i })

        await expect.element(noDataProvided).toBeInTheDocument()
    })

    test('Show data correctly', async ({ worker }) => {
        worker.use(
            http.get('/api/tasks', () => {
                return HttpResponse.json(defaultTasks, { status: 200 })
            })
        )
        await new Promise(resolve => setTimeout(resolve, 1000));
        await render(setup())

        await expect.element(page.getByText('Name')).toBeInTheDocument()
        await expect.element(page.getByText('Due Date')).toBeInTheDocument()
        await expect.element(page.getByText('Priority')).toBeInTheDocument()
        await expect.element(page.getByText('Status')).toBeInTheDocument()

        for (const taks of defaultTasks) {
            await expect.element(page.getByText(taks.name)).toBeInTheDocument()
            await expect.element(page.getByText('Due Date')).toBeInTheDocument()
            await expect.element(page.getByText(taks.priority)).toBeInTheDocument()
            await expect.element(page.getByText(taks.status?.toString())).toBeInTheDocument()
        }
    })

    test('create new task', async ({ worker }) => {
        const submitSpy = vi.fn()
        worker.use(
            http.get('/api/tasks', () => {
                return HttpResponse.json(defaultTasks, { status: 200 })
            }),
            http.post('/api/tasks', () => {
                submitSpy()
                return HttpResponse.json({}, {status: 201})
            })
        )
        await new Promise(resolve => setTimeout(resolve, 1000));
        await render(setup())

        await page.getByRole('button', {name: /Add/i}).click()
        await page.getByLabelText('Name').fill('Test Task')
        await page.getByLabelText('Description').fill('Test description')
        await page.getByLabelText('Priority').click()
        await page.getByText('High').nth(1).click()

        await page.getByRole('button', {name: 'Save'}).click()

        await expect(submitSpy).toHaveBeenCalled()
    })

    test('update new task', async ({ worker }) => {
        const submitSpy = vi.fn()
        worker.use(
            http.get('/api/tasks', () => {
                return HttpResponse.json(defaultTasks, { status: 200 })
            }),
            http.put('/api/tasks/:taskId', () => {
                submitSpy()
                return HttpResponse.json({}, {status: 201})
            })
        )
        await new Promise(resolve => setTimeout(resolve, 1000));
        await render(setup())

        await page.getByTestId(`dropdown-${defaultTasks[0].id}`).click()
        await page.getByText('Update').click()
        await page.getByLabelText('Name').fill('Test update')
        await page.getByLabelText('Description').fill('Test description update')
        await page.getByLabelText('Priority').click()
        await page.getByText('Critical').click()

        await page.getByRole('button', {name: 'Save'}).click()

        await expect(submitSpy).toHaveBeenCalled()
    })
})