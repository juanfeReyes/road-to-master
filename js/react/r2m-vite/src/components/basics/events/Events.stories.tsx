import type { Meta, StoryObj } from '@storybook/react-vite';

import { Events } from './Events';
import { expect, fireEvent } from 'storybook/internal/test';

const meta = {
  component: Events,
} satisfies Meta<typeof Events>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {},
  play: async ({ canvas, userEvent, step }) => {
    await step('Mouse event render', async () => {
      await expect(canvas.getByTestId('mouse_click_btn')).toBeInTheDocument()
      await expect(canvas.getByTestId('mouse_drag_zoneA')).toBeInTheDocument()
      await expect(canvas.getByTestId('mouse_drag_textA')).toBeInTheDocument()
      await expect(canvas.getByTestId('mouse_drag_zoneA')).toBeInTheDocument()
      await expect(canvas.getByTestId('mouse_btn')).toBeInTheDocument()
    })
    await step('Clipboard event render', async () => {
      await expect(canvas.getByTestId('clipboard_copy_input')).toBeInTheDocument()
      await expect(canvas.getByTestId('clipboard_paste_input')).toBeInTheDocument()
    })
    await step('Focus event render', async () => {
      await expect(canvas.getByTestId('focus_input')).toBeInTheDocument()
    })
    await step('Form event render', async () => {
      await expect(canvas.getByTestId('form_input')).toBeInTheDocument()
      await expect(canvas.getByTestId('form_submit_btn')).toBeInTheDocument()
      await expect(canvas.getByTestId('form_reset_btn')).toBeInTheDocument()
    })
    await step('Keyboard event render', async () => {
      await expect(canvas.getByTestId('keyboard_event_input')).toBeInTheDocument()
    })
  }
};

export const MouseEvents: Story = {
  play: async ({ canvas, step, userEvent }) => {
    await step('Click events', async () => {
      const clickBtn = await canvas.getByTestId('mouse_click_btn')
      await userEvent.click(clickBtn);
      await expect(canvas.getByTestId('notification_msg')).toBeInTheDocument()
      await expect(canvas.getByTestId('notification_msg')).toHaveTextContent('Click notification')

      await userEvent.dblClick(clickBtn);
      await expect(canvas.getByTestId('notification_msg')).toBeInTheDocument()
      await expect(canvas.getByTestId('notification_msg')).toHaveTextContent('Double click notification')
    })
    await step('Drag events', async () => {
      const dragElem = await canvas.getByTestId('mouse_drag_textA')
      const dragZoneB = await canvas.getByTestId('mouse_drag_zoneB')
      // await userEvent.pointer([
      //   {keys: '[MouseLeft>]', target: dragElem},
      //   {keys: 'MouseLeft', target: dragZoneB},
      //   {keys: '[/MouseLeft]'}
      // ])
      await fireEvent.mouseDown(dragElem, { clientX: dragElem.getBoundingClientRect().left, clientY: dragElem.getBoundingClientRect().top });
      await fireEvent.mouseMove(dragElem, { clientX: dragZoneB.getBoundingClientRect().left+50, clientY: dragZoneB.getBoundingClientRect().top+50 });
      await fireEvent.mouseUp(dragElem);
      // TODO: RTL does not support drag and drop events
      // await expect(canvas.getByTestId('mouse_drag_textB')).toBeInTheDocument()
    })
    await step('mouse events', async () => {
      const mouseZone = await canvas.getByTestId('mouse_event_zone')
      await userEvent.hover(mouseZone);
      await expect(canvas.getByText('Mouse move')).toBeInTheDocument()
      await userEvent.unhover(mouseZone);
      await expect(canvas.getByText('Mouse leave')).toBeInTheDocument()

      const mouseBtn = await canvas.getByTestId('mouse_btn')
      await fireEvent.mouseDown(mouseBtn);
      await expect(canvas.getByText('Mouse down')).toBeInTheDocument()
      await fireEvent.mouseUp(mouseBtn);
      await expect(canvas.getByText('Mouse up')).toBeInTheDocument()
    })
  }
}

export const ClipboardEvents: Story = {
  play: async ({canvas, userEvent }) => {
    const copyInput = await canvas.findByTestId('clipboard_copy_input')
    const pasteInput = await canvas.findByTestId('clipboard_paste_input')
    
    await userEvent.type(copyInput, 'test')
    await fireEvent.copy(copyInput)
    await expect(canvas.getByText('Copy event')).toBeInTheDocument()

    pasteInput.focus()
    await userEvent.paste()
    await expect(canvas.getByText('Paste event')).toBeInTheDocument()
  }
} 

export const FocusEvents: Story = {
  play: async ({canvas, userEvent}) => {
    const mouseBtn = await canvas.getByTestId('mouse_btn')
    await userEvent.click(mouseBtn)
    const focusInput = await canvas.getByTestId('focus_input')
    await userEvent.click(focusInput)
    await expect(canvas.getByText('Focus event')).toBeInTheDocument()

    await userEvent.click(mouseBtn)
    await expect(canvas.getByText('Blur event')).toBeInTheDocument()
  }
}

export const FormEvents: Story = {
  play: async ({canvas, userEvent}) => {
    const formInput = await canvas.getByTestId('form_input')
    const submitButton = await canvas.getByTestId('form_submit_btn')
    const resetButton = await canvas.getByTestId('form_reset_btn')

    await userEvent.type(formInput, '1')
    await expect(canvas.getByText('Change event 1')).toBeInTheDocument()
    await userEvent.click(submitButton)
    await expect(canvas.getByText('Submit event')).toBeInTheDocument()

    await userEvent.type(formInput, '11111')
    await userEvent.click(submitButton)
    await expect(canvas.getByText('invalid event 111111')).toBeInTheDocument()

    await userEvent.click(resetButton)
    await expect(canvas.getByText('Reset event')).toBeInTheDocument()
  }
}

export const KeyboardEvents: Story = {
  play: async ({canvas, userEvent}) => {
    const keyboardInput = await canvas.getByTestId('keyboard_event_input')

    await userEvent.click(keyboardInput)
    await userEvent.keyboard("{a>}")
    await expect(canvas.getByText('Key down event a')).toBeInTheDocument()

    await userEvent.keyboard("{/a}")
    await expect(canvas.getByText('Key up event a')).toBeInTheDocument()
  }
}

