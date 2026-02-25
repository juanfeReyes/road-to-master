'use client'
import React, { useState } from "react";
import MemoryCard from "../QuestionCard/QuestionCard";
import { Question } from "@/src/features/common/model/Question";
import { useGameStore } from "../../../common/store/GameStore";
import { useRouter } from "next/navigation";
import { Icon } from "@iconify/react";
import clsx from "clsx";
import { defineStepper } from "@stepperize/react";
import SwitchCard from "../SwitchCard/SwitchCard";
import { Button } from "@/src/features/common/components/Button/Button";

export const GameLayout = () => {
    const router = useRouter()

    const { currentGame, resetGame, finishGame } = useGameStore()
    const [correctAnswers, setCorrectAnswers] = useState<Question[]>([])
    const [wrongAnswers, setWrongAnswers] = useState<Question[]>([])
    if (currentGame === null) {

        router.push("/")
        return;
    }
    const { questions } = currentGame
    const { useStepper } = defineStepper(...questions.map((q, idx) => ({ id: idx.toString(), ...q })))
    const { state, navigation, flow } = useStepper({ initialStep: '0' })

    const completeGame = () => {
        const report = {
            wrong: wrongAnswers,
            correct: correctAnswers
        }
        // setCurrentGame({ ...currentGame, report: report })
        router.push("/game/report")
    }

    const nextQuestion = () => {
        if (state.isLast) {
            completeGame()
            return;
        }
        navigation.next()
    }

    const onCorrectAnswer = () => {
        setCorrectAnswers(ans => [...ans, questions[Number(state.current.data.id)]])
        nextQuestion()
    }

    const onWrongAnswer = () => {
        setWrongAnswers(ans => [...ans, questions[Number(state.current.data.id)]])
        nextQuestion()
    }

    const handleReset = () => {
        router.push("/")
    }

    return (<div className="flex flex-col gap-2">
        <header className="bg-cyan-500">
            timer + reset +
        </header>
        <div className="flex">
            <nav className="w-1/5">
                <div className="flex flex-col gap-2">
                    <div className="border-2 rounded-br-full">{Number(state.current.data.id)-1}</div>
                    <div className="border-2 rounded-br-full">{Number(state.current.data.id)}</div>
                    <div className="border-2 rounded-br-full">{Number(state.current.data.id)+1}</div>
                </div>
            </nav>
            <main className="w-3/5">
                <SwitchCard>
                    <SwitchCard.Primary>
                        {state.current.data.question}
                    </SwitchCard.Primary>
                    <SwitchCard.Secondary>
                        {state.current.data.answer}
                        <div>
                            <Button type="Info" onClick={onCorrectAnswer} label="Ok" />
                            <Button type="Error" onClick={onWrongAnswer} label="Bad" />
                        </div>
                    </SwitchCard.Secondary>
                </SwitchCard>
            </main>
            <aside className="w-1/5">metadata</aside>
        </div>
    </div>)
}
