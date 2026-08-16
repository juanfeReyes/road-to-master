import { describe, expect, test, vi } from "vitest"
import { render } from "vitest-browser-react"
import { CustomInput } from "./CustomInput"
import { page } from "vitest/browser"

describe('Custom Input should', () => {
    test('render correctly', async () => {
        let value = ""
        const onChangeHandler = (val: string) => { value = val }
        render(<CustomInput label={"Test-Label"} value={""}
            onChange={onChangeHandler} error={undefined} placeholder="test placeholder" />)

        const label = page.getByText('Test-Label')
        const input = page.getByPlaceholder('test placeholder')

        await expect.element(label).toBeInTheDocument()
        await expect.element(input).toBeInTheDocument()
    })

    test('call change handler and update value', async () => {
        let value = "jeje"
        const onChangeHandler = vi.fn((val: string) => { value = val })
        await render(<CustomInput label={"Test-Label"} value={value}
            onChange={onChangeHandler} error={undefined} placeholder="test placeholder" />)

        const input = page.getByPlaceholder('test placeholder')

        const newValue = 'Testing the custom value'
        await input.fill(newValue)

        await expect(onChangeHandler).toHaveBeenCalled()
        await expect(onChangeHandler).toHaveBeenCalledWith(newValue)
    })

    test('show error', async () => {
        const errorMsg = 'Test error'
        let value = "jeje"
        const onChangeHandler = vi.fn((val: string) => { value = val })
        await render(<CustomInput label={"Test-Label"}

            value={value}
            onChange={onChangeHandler}
            error={errorMsg}
            placeholder="test placeholder" />)

        const error = page.getByText(errorMsg)

        await expect.element(error).toBeInTheDocument()
    })
})