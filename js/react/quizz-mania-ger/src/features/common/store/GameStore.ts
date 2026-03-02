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

export type SetupActionType = {
    id?: string
    sortBy: sortOptionLabelTypes
    timer: number | undefined,
    questions: Question[],
    resume: Partial<Record<string | number, GameQuestion[]>>
}

export type FinishGameActionType = {
    correct: Question[],
    wrong: Question[]
}

interface GameState {
    config: any,
    currentGame: Game | null,
    lastGames: Game[],
    setConfig: (config: any) => void,
    initializeGame: (setup: SetupActionType) => void,
    startGame: () => void,
    finishGame: (action: FinishGameActionType) => void,
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
    initializeGame: (setup: SetupActionType) => {
        const totalQuestions = setup.questions.length
        set({
            currentGame: {
                id: setup.id ?? v4(),
                setup: {
                    timer: { minutes: setup.timer },
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
    finishGame: (action: FinishGameActionType) => set(
        produce((s: GameState) => {
            if (s.currentGame && s.currentGame.report) {
                s.currentGame.report = {
                    startDate: s.currentGame.report?.startDate,
                    endDate: new Date(),
                    correct: action.correct,
                    wrong: action.wrong
                }
                s.lastGames.push(s.currentGame)
                s.currentGame = null
            }
        })
    ),
    resetGame: () => set(initialState)
}))
