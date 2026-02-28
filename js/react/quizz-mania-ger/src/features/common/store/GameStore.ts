import { GameQuestion, Question } from "@/src/features/common/model/Question";
import { produce } from "immer";
import { create } from "zustand";
import { sortOptionLabelTypes } from "../model/Options";
import { UUIDTypes, v4 } from "uuid";

/** change domain to contain the questions and questions should be the flattened */
interface Game {
    id: UUIDTypes
    questions: Question[],
    setup: {
        sortBy: sortOptionLabelTypes,
        totalQuestions: number,
        timer?: {
            minutes?: number,
            expireTime?: Date,
        }
        questResume: Partial<Record<string | number, GameQuestion[]>>
    },
    report?: {
        startDate: Date,
        endDate?: Date,
        correct?: Question[],
        wrong?: Question[]
    }
}

export type SetupType = {
    sortBy: sortOptionLabelTypes
    timer: number | undefined,
    questions: Question[],
    resume: Partial<Record<string | number, GameQuestion[]>>
}

interface GameState {
    config: any,
    currentGame: Game | null,
    lastGames: Game[],
    setConfig: (config: any) => void,
    initializeGame: (setup: SetupType) => void,
    startGame: () => void,
    finishGame: () => void,
    resetGame: () => void
}

const initialState = {
    config: null,
    currentGame: null,
    lastGames: [],
}

export const useGameStore = create<GameState>(set => ({
    ...initialState,
    setConfig: (config: any) => set({ config: config }),
    initializeGame: (setup: SetupType) => {
        const totalQuestions = setup.questions.length
        set({
            currentGame: {
                id: v4(),
                setup: {
                    timer: {minutes: setup.timer},
                    questResume: setup.resume,
                    totalQuestions: totalQuestions,
                    sortBy: setup.sortBy
                },
                questions: setup.questions
            }
        })
    },
    startGame: () =>
        set(produce((s: GameState) => {
            if (s.currentGame) {
                const startDate = new Date()
                s.currentGame.report = { startDate }
                if (s.currentGame.setup.timer && s.currentGame.setup.timer.minutes) {
                    const expireTime = new Date(startDate);
                    expireTime.setMinutes(expireTime.getMinutes() + s.currentGame.setup.timer.minutes)
                    s.currentGame.setup.timer.expireTime = expireTime
                }
            }
        })),
    finishGame: () => { },
    resetGame: () => set(initialState)
}))
