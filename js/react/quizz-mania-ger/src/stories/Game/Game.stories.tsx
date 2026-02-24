import { GameLayout } from "@/src/features/game/components/GameLayout/GameLayout";
import type { Meta, StoryObj } from '@storybook/nextjs-vite'
import { useGameStore } from "@/src/features/common/store/GameStore";
import { useEffect } from "react";
import { StoryFn } from "storybook/internal/types";
import { useShuffler } from "@/src/features/setgeneration/hooks/useShuffler";
import { useFetcher } from "@/src/features/setgeneration/hooks/useFetcher";


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
            const setCurrentGame = useGameStore(state => state.setCurrentGame)
            const {fetchAndShuffle} = useShuffler()
            const {fetchDomains} = useFetcher()
            useEffect(() => {
                const getQuestions = async () => {
                    const questions = await fetchAndShuffle([{file: '/english/java/core.yml', name: 'story'}], 'By domain', Number.MAX_SAFE_INTEGER)
                    const domains = await fetchDomains([{file: '/english/java/core.yml', name: 'story'}])
                    setCurrentGame({ questions: questions, domains: domains })
                }

                getQuestions()
            }, [])

            return <Story />
        }
    ]
}