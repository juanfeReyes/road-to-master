import QuizzCard from "@/components/QuizzCard/QuizzCard";
import type {Meta, StoryObj} from '@storybook/nextjs-vite'


const meta = {
    component: QuizzCard
} satisfies Meta<typeof QuizzCard>

export default meta;
type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        question: 'How should this work',
        answer: "Should work as memory card"
    }
}