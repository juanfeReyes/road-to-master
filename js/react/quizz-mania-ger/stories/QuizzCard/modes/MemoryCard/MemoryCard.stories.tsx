import MemoryCard from "@/components/QuizzCard/modes/MemoryCard/MemoryCard";
import type {Meta, StoryObj} from '@storybook/nextjs-vite'


const meta = {
    component: MemoryCard
} satisfies Meta<typeof MemoryCard>

export default meta;
type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        question: 'How should this work',
        answer: "Should work as memory card",
        
    }
}