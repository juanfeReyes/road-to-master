import {describe, expect, test, vi} from 'vitest'
import { render } from 'vitest-browser-react'
import { Button } from './Button'
import { page } from 'vitest/browser'


describe('Button should', () => {
    test('render button', async () => {
        const clickCallback = vi.fn()
        const label = 'Test'
        render(<Button label={label} type='Primary' onClick={clickCallback}/>)
        const button = page.getByRole('button', {name: /Test/i})
        
        await expect.element(button).toBeInTheDocument()
        await expect.element(button).toHaveTextContent(label)
    })

    test('call prop handler button', async () => {
        const clickCallback = vi.fn()
        const label = 'Test'
        render(<Button label={label} type='Primary' onClick={clickCallback}/>)
        const button = page.getByRole('button', {name: /Test/i})

        await button.click()
        
        await expect(clickCallback).toHaveBeenCalledOnce()
    })
})
