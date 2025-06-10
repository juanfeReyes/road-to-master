import type { Meta, StoryObj } from '@storybook/react-vite';

import { expect } from 'storybook/test'

import { Basics } from './Basics';

const meta = {
  component: Basics,
  beforeEach: () => {
    localStorage.removeItem('count')
  }
} satisfies Meta<typeof Basics>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {
  play: async ({ canvas, userEvent }) => {
    await expect(canvas.getByTestId('counter')).toBeInTheDocument()
    await expect(canvas.getByTestId('increase-btn')).toBeInTheDocument()
    await expect(canvas.getByTestId('decrease-btn')).toBeInTheDocument()
    await expect(canvas.getByTestId('increase-btn')).toHaveFocus()
  }
} satisfies Story;

export const IncreaseCounter: Story = {
  play: async ({ canvas, userEvent }) => {
    await expect(canvas.getByTestId('counter')).toBeInTheDocument()
    await userEvent.click(canvas.getByTestId('increase-btn'))
    await expect(canvas.getByTestId('counter')).toHaveTextContent('count: 1')
  }
} satisfies Story;

export const DecreaseCounter: Story = {
  play: async ({ canvas, userEvent }) => {
    await expect(canvas.getByTestId('counter')).toBeInTheDocument()
    await userEvent.click(canvas.getByTestId('decrease-btn'))
    await expect(canvas.getByTestId('counter')).toHaveTextContent('count: -1')
  }
} satisfies Story;