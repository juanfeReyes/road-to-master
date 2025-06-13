import type { Meta, StoryObj } from '@storybook/react-vite';

import { Reusability } from './Reusability';

const meta = {
  component: Reusability,
} satisfies Meta<typeof Reusability>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {}
};