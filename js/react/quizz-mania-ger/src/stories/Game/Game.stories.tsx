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
            

            return <Story />
        }
    ]
}