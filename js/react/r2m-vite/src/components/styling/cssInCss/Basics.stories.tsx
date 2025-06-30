import type { Meta, StoryObj } from '@storybook/react-vite';

import { SkillList } from './Basics';

const meta = {
  component: SkillList,
} satisfies Meta<typeof SkillList>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {}
};