'use client'
import React, { useState } from "react";
import MemoryCard from "../QuestionCard/QuestionCard";
import { Question } from "@/src/features/common/model/Question";
import { useGameStore } from "../../../common/store/GameStore";
import { useRouter } from "next/navigation";
import { Icon } from "@iconify/react";
import clsx from "clsx";

export const GameLayout = () => {
    const router = useRouter()
    const { currentGame, setCurrentGame, reset } = useGameStore()
    const [currentQuestion, setCurrentQuestions] = useState(0)
    const [correctAnswers, setCorrectAnswers] = useState<Question[]>([])
    const [wrongAnswers, setWrongAnswers] = useState<Question[]>([])
    if (currentGame === null) {

        router.push("/")
        return;
    }
    const { questions } = currentGame

    const completeGame = () => {
        const report = {
            wrong: wrongAnswers,
            correct: correctAnswers
        }
        setCurrentGame({ ...currentGame, report: report })
        router.push("/game/report")
    }

    const nextQuestion = () => {
        if (currentQuestion === questions.length - 1) {
            completeGame()
            return;
        }
        setCurrentQuestions(curr => curr + 1)
    }

    const onCorrectAnswer = () => {
        setCorrectAnswers(ans => [...ans, questions[currentQuestion]])
        nextQuestion()
    }

    const onWrongAnswer = () => {
        setWrongAnswers(ans => [...ans, questions[currentQuestion]])
        nextQuestion()
    }

    const handleReset = () => {
        reset()
        router.push("/")
    }
    const question = questions[currentQuestion];
    const currentDomain = currentGame.domains[question.domainIdx]

    return (<div className="flex flex-col gap-1">
        <div className="flex gap-1 pb-2 justify-between">
            <div className="flex justify-center items-center">
                <Icon icon="lets-icons:check-fill" className={'text-2xl'} />
                {correctAnswers.length}
            </div>
            <button onClick={handleReset}><Icon icon="ri:reset-right-fill" className={'text-2xl transform -scale-x-100'} /></button>
        </div>
        <div className="pt-8 flex gap-2">
            <div className="flex justify-center">
                <MemoryCard
                    question={questions[currentQuestion]}
                    onCorrectAnswer={onCorrectAnswer}
                    onWrongAnswer={onWrongAnswer} />
            </div>
            <div className="min-w-1/6 bg-blue-300 p-2 ml-auto rounded-md">
                <div className="flex justify-end">
                    {Array(currentDomain.level).fill(0).map(item => <Icon icon={"streamline-flex:skull-2-solid"} />)}
                </div>
                <p className="py-2 font-bold">{currentDomain.name}</p>
                <ul className="flex flex-wrap gap-1">
                    {currentDomain.tags.map(tag => (<li className="bg-gray-100 rounded-xl text-sm p-1">{tag}</li>))}
                </ul>
            </div>
        </div>
    </div>)
}
