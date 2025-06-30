import type { Meta, StoryObj } from '@storybook/react-vite';

import { Position } from './Position';

const meta = {
  component: Position,
} satisfies Meta<typeof Position>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {}
};