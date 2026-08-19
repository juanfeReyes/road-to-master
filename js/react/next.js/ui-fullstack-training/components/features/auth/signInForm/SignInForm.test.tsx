
import { MainProvider } from "@/components/common/layout/mainProvider/mainProvider";
import { test } from "@/lib/test/browser-extend";
import { describe, expect, vi } from "vitest";
import { render } from "vitest-browser-react";
import { page } from "vitest/browser";
import { http, HttpResponse } from "msw";
import { SignInForm } from "./SignInForm";

vi.mock('next/navigation', () => {
    const actual = vi.importActual('next/navigation');

    const mockRouter = {
        push: vi.fn(),
        replace: vi.fn(),
        refresh: vi.fn(),
        back: vi.fn(),
        forward: vi.fn(),
        prefetch: vi.fn(),
    };

    return {
        ...actual,
        useRouter: () => mockRouter,
        usePathname: vi.fn(() => '/'),
        useSearchParams: vi.fn(() => new URLSearchParams()),
        useParams: vi.fn(() => ({})),
        // This default export structure satisfies Vitest/Vite module parsing
        default: {
            useRouter: () => mockRouter,
            usePathname: vi.fn(() => '/'),
            useSearchParams: vi.fn(() => new URLSearchParams()),
            useParams: vi.fn(() => ({})),
        },
    };
});

const setup = () => <MainProvider>
    <SignInForm />
</MainProvider>

describe('SignIn form should', () => {

    test('Show invalid email error', async () => {
        await new Promise(resolve => setTimeout(resolve, 1000));
        await render(setup())

        await page.getByLabelText('Email').fill('test')
        await page.getByRole('button', {name: 'Sign In'}).click()

        await expect.element(page.getByText('Invalid email address', { exact: true })).toBeInTheDocument()
    })

    test('Show too short password error', async () => {
        await new Promise(resolve => setTimeout(resolve, 1000));
        await render(setup())

        await page.getByLabelText('Password').nth(0).fill('123')
        await page.getByRole('button', {name: 'Sign In'}).click()

        await expect.element(page.getByText('Password is too short', { exact: true })).toBeInTheDocument()
    })

    test('SignUp user correctly', async ({worker}) => {
        const submitSpy = vi.fn()
        worker.use(
            http.post('/api/auth/sign-in/email', () => {
                submitSpy()
                return HttpResponse.json({}, { status: 200 })
            })
        )
        await new Promise(resolve => setTimeout(resolve, 1000));
        await render(setup())

        await page.getByLabelText('Email').fill('test@mail.com')
        await page.getByLabelText('Password').nth(0).fill('123456789')
        await page.getByRole('button', {name: 'Sign In'}).click()

        await expect(submitSpy).toHaveBeenCalled()
    })
})
