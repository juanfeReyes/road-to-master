import type { Meta, StoryObj } from '@storybook/react-vite';

import { Hooks } from './Hooks';

const meta = {
  component: Hooks,
} satisfies Meta<typeof Hooks>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {}
};