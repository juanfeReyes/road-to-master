import type { Meta, StoryObj } from '@storybook/react-vite';

import { Refs } from './Refs';

const meta = {
  component: Refs,
} satisfies Meta<typeof Refs>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {}
};