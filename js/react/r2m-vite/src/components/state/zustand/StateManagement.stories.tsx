import type { Meta, StoryObj } from '@storybook/react-vite';

import { StateManagement } from './StateManagement';

const meta = {
  component: StateManagement,
} satisfies Meta<typeof StateManagement>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {}
};