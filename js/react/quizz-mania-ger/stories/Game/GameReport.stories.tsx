import type {Meta, StoryObj} from '@storybook/nextjs-vite'
import javaDomain from '../../assets/quizes/java/core.json'
import { GameReport } from "@/components/Game/GameReport";


const meta = {
    component: GameReport
} satisfies Meta<typeof GameReport>

export default meta;
type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        correctAnswers: javaDomain.quizz,
        wrongAnswers: javaDomain.quizz,
    }
}