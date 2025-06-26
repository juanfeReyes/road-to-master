import type { Meta, StoryObj } from '@storybook/react-vite';
import { withRouter, reactRouterParameters } from 'storybook-addon-remix-react-router'


import HomeForm from './HomeForm';

const meta = {
  component: HomeForm,
  decorators: [
    withRouter
  ],
} satisfies Meta<typeof HomeForm>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {
  parameters: reactRouterParameters({
    routing: { path: '/homes' },
  })
};