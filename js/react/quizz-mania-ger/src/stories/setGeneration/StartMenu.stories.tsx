import { StartMenu } from '@/src/features/setgeneration/components/StartMenu/StartMenu';
import type {Meta, StoryObj} from '@storybook/nextjs-vite'


const meta = {
    component: StartMenu,
    parameters: {
        nextjs: {
            appDirectory: true,
        },
    }
} satisfies Meta<typeof StartMenu>

export default meta;
type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        correctAnswers: [],
        wrongAnswers: [],
    }
}