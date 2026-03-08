import { QuestionResult } from "@/src/features/common/model/Question"
import { useGameStore, SetupActionType } from "@/src/features/common/store/GameStore"
import { ReportLayout } from "@/src/features/reporting/components/ReportLayout/ReportLayout"
import { Meta, StoryObj } from "@storybook/nextjs-vite"
import { useEffect } from "react"

const answers: QuestionResult[] = [
    {
        question: "Question for testing Number 1",
        answer: "Answer for test number 1",
        level: 1,
        tags: ['Tag1', 'Tag2', 'Tag3'],
        domainName: 'Java',
        role: 'Java Backend',
        result: 'CORRECT'
    },
    {
        question: "Question for testing Number 2",
        answer: "Check and validate the answer for question 2",
        level: 2,
        tags: ['Tag extra looooooooooooooooooooooong', 'T', 'Wouuuuuuuuuuuuuuuuuuuuuuu'],
        domainName: '.Net',
        role: 'DotNET Backend',
        result: 'CORRECT'
    },
    {
        question: "Question for testing Number 3",
        answer: "Check and validate the answer for question 3",
        level: 3,
        tags: [],
        domainName: 'Java',
        role: 'Java Backend',
        result: 'WRONG'
    },
    {
        question: "Question for testing Number 1",
        answer: "Answer for test number 1",
        level: 1,
        tags: ['Tag1', 'Tag2', 'Tag3'],
        domainName: 'Java',
        role: 'Java Backend',
        result: 'CORRECT'
    },
    {
        question: "Question for testing Number 2",
        answer: "Check and validate the answer for question 2",
        level: 2,
        tags: ['Tag extra looooooooooooooooooooooong', 'T', 'Wouuuuuuuuuuuuuuuuuuuuuuu'],
        domainName: '.Net',
        role: 'DotNET Backend',
        result: 'WRONG'
    }
]

const meta = {
    decorators: [
        (Story) => {

            const { initializeGame, startGame, finishGame, currentGame } = useGameStore()

            useEffect(() => {
                const setup: SetupActionType = {
                    id: '123',
                    sortBy: "By domain",
                    timer: undefined,
                    questions: [],
                    resume: {}
                }
                initializeGame(setup)
                startGame()
                finishGame({answers})

            }, [])

            return <Story />
        }
    ],
    component: ReportLayout,
    parameters: {
        nextjs: {
            appDirectory: true
        },
    }
} satisfies Meta<typeof ReportLayout>

export default meta;
type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        open: true
    },
    parameters: {
        nextjs: {
            navigation: {
                query: {
                    id: '123'
                }
            }
        }
    }
}