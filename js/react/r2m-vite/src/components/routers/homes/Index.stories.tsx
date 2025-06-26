import type { Meta, StoryObj } from '@storybook/react-vite';
import {withRouter, reactRouterParameters} from 'storybook-addon-remix-react-router'
import { initialize, mswLoader } from 'msw-storybook-addon'

// initialize({}, [...handlers])

import Index, { clientLoader } from './Index';
import { handlers } from '../../../mocks/handlers';

const meta = {
  component: Index,
  decorators:[
    withRouter
  ],
  // loaders: [mswLoader]
} satisfies Meta<typeof Index>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {
    loaderData: {homes: [{id: '123', name: 'test', address: 'cra 123'}]}
  },
  parameters: {
    reactRouter: reactRouterParameters({
      routing: {
        loader: () => ({homes: []}),
        path: '/homes'
      }
    })
  }
};