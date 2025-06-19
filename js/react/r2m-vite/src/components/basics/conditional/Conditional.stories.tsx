import type { Meta, StoryObj } from '@storybook/react-vite';

import { expect } from 'storybook/test'

import { Conditional, conditionOptions} from './Conditional';

const meta = {
  component: Conditional,
  beforeEach: () => {
  },
  argTypes: {
    option: {
      options: Object.values(conditionOptions)
    }
  }
} satisfies Meta<typeof Conditional>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {
    showCondition: false
  }
} satisfies Story;

export const ShowConditional: Story = {
  args: {
    showCondition: true
  }
}

export const SwitchConditional: Story = {
  args: {
    option: 'Ok'
  }
}

export const HighOrderConditional: Story = {
  args: {
    option: 'Error'
  }
}
