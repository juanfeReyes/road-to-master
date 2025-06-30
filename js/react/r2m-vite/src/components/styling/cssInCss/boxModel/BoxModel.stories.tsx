import type { Meta, StoryObj } from '@storybook/react-vite';

import { BoxModel } from './BoxModel';

const meta = {
  component: BoxModel,
} satisfies Meta<typeof BoxModel>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {}
};