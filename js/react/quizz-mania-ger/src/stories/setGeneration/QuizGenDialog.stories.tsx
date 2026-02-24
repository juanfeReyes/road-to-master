import { QuizGenDialog } from "@/src/features/setgeneration/components/QuizGenDialog/QuizGenDialog";
import { useFetcher } from "@/src/features/setgeneration/hooks/useFetcher";
import type { Meta, StoryObj} from "@storybook/nextjs-vite";
import { useEffect } from "react";


const meta = {
    decorators: [
        (Story) => {

            const { setConfigManifest } = useFetcher()
            useEffect(() => {
                setConfigManifest()
            })

            return <Story />
        }
    ],
    component: QuizGenDialog,
    parameters: {
        nextjs: {
            appDirectory: true,
        },
    }
} satisfies Meta<typeof QuizGenDialog>

export default meta;
type Story = StoryObj<typeof meta>

export const Default: Story = {
    args: {
        open: true
    }
}

export const Resume: Story = {
    args: {
        open: true,
        initialStep: 'resume'
    }
}

