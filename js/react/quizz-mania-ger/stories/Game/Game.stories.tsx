import { Game } from "@/components/Game/Game";
import type {Meta, StoryObj} from '@storybook/nextjs-vite'
import javaDomain from '../../assets/quizes/java/core.json'


const meta = {
    component: Game
} satisfies Meta<typeof Game>

export default meta;
type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        questions: javaDomain.quizz
    }
}