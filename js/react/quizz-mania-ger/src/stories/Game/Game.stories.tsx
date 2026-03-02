import { GameLayout } from "@/src/features/game/components/GameLayout/GameLayout";
import type { Meta, StoryObj } from '@storybook/nextjs-vite'
import { SetupActionType, useGameStore } from "@/src/features/common/store/GameStore";
import { useEffect } from "react";
import { StoryFn } from "storybook/internal/types";
import { Question } from "@/src/features/common/model/Question";

const questions: Question[] = [
    {
        question: "Question for testing Number 1",
        answer: "Answer for test number 1",
        level: 1,
        tags: ['Tag1', 'Tag2', 'Tag3']
    },
    {
        question: "Question for testing Number 2",
        answer: "Check and validate the answer for question 2",
        level: 2,
        tags: ['Tag extra looooooooooooooooooooooong', 'T', 'Wouuuuuuuuuuuuuuuuuuuuuuu']
    },
    {
        question: "Question for testing Number 3",
        answer: "Check and validate the answer for question 3",
        level: 3,
        tags: []
    }
]


const meta = {
    component: GameLayout,
    parameters: {
        nextjs: {
            appDirectory: true,
        },
    }
} satisfies Meta<typeof GameLayout>

export default meta;
type Story = StoryObj<typeof meta>

export const Default: Story = {
    decorators: [
        (Story: StoryFn) => {
            const { initializeGame, startGame } = useGameStore()

            useEffect(() => {
                const setup: SetupActionType = {
                    sortBy: "By domain",
                    timer: undefined,
                    questions: questions,
                    resume: {}
                }
                initializeGame(setup)
                startGame()

            }, [])

            return <Story />
        }
    ]
}

export const WithTimer: Story = {
    decorators: [
        (Story: StoryFn) => {
            const { initializeGame, startGame } = useGameStore()

            useEffect(() => {
                const setup: SetupActionType = {
                    sortBy: "By domain",
                    timer: 1,
                    questions: questions,
                    resume: {}
                }
                initializeGame(setup)
                startGame()

            }, [])

            return <Story />
        }
    ]
}