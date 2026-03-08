'use client'
import React, { ComponentProps, PropsWithChildren, useState } from "react";
import MemoryCard from "../QuestionCard/QuestionCard";
import { GameQuestion, Question, QuestionResult, QuestionState } from "@/src/features/common/model/Question";
import { useGameStore } from "../../../common/store/GameStore";
import { useRouter } from "next/navigation";
import { Icon } from "@iconify/react";
import clsx from "clsx";
import { defineStepper } from "@stepperize/react";
import SwitchCard from "../SwitchCard/SwitchCard";
import { Button } from "@/src/features/common/components/Button/Button";
import { useStopwatch, useTimer } from "react-timer-hook";
import { toast, ToastContainer } from "react-toastify";

type CircleProps = ComponentProps<'div'> & {
    width: '5' | '7' | '10' | '15'
}
const Circle = ({ children, width, onClick }: PropsWithChildren<CircleProps>) => {
    return (
        <div onClick={onClick} className={`border-2 rounded-full w-${width} aspect-square flex items-center justify-center`}>
            <p>{children}</p>
        </div>
    )
}

type TimerProps = {
    expireTime: Date,
    onExpire: () => void
}
const Timer = ({ expireTime, onExpire }: TimerProps) => {
    const { minutes, seconds } = useTimer({ expiryTimestamp: expireTime, onExpire: onExpire })

    return (<div className="flex items-center gap-2 justify-center text-2xl">
        <Icon icon={"gg:timer"} />{String(minutes).padStart(2, '0')}:{String(seconds).padStart(2, '0')}
    </div>)
}

type StopWatchProps = {
}
const StopWatch = ({ }: StopWatchProps) => {
    const { minutes, seconds } = useStopwatch({ autoStart: true })

    return (<div className="flex items-center gap-2 justify-center text-2xl">
        <Icon icon={"boxicons:stopwatch"} />{String(minutes).padStart(2, '0')}:{String(seconds).padStart(2, '0')}
    </div>)
}

export const GameLayout = () => {
    const router = useRouter()
    const { currentGame, resetGame, finishGame } = useGameStore()
    const [answers, setAnswers] = useState<QuestionResult[]>([])
    if (currentGame === null) {

        router.push("/")
        return;
    }
    const { questions } = currentGame
    const { useStepper } = defineStepper(...questions.map((q, idx) => ({ id: idx.toString(), ...q })))
    const { state, navigation } = useStepper({ initialStep: '0' })
    const currentIdx = Number(state.current.data.id)


    const completeGame = () => {
        finishGame({ answers })
        toast('Game completed')
        router.push(`/game/report?id=${currentGame?.id}`)
    }

    const nextQuestion = () => {
        if (state.isLast) {
            completeGame()
            return;
        }
        navigation.next()
    }

    const onAnswer = (result: QuestionState) => {
        const answer: QuestionResult = {...questions[currentIdx], result}
        setAnswers(ans => [...ans, answer])
        nextQuestion()
    }

    const handleReset = () => {
        resetGame()
        router.push("/")
    }

    return (<>
        <ToastContainer />
        <div className="flex flex-col gap-10 bg-stone-100 ">
            <header className="bg-cyan-500 flex items-center p-2 rounded-lg justify-between">
                {currentGame.setup.timer?.expireTime ?
                    <Timer expireTime={currentGame.setup.timer.expireTime} onExpire={completeGame} /> :
                    <StopWatch />}
                <div className="flex gap-5">
                    <p className="text-xl flex"><p className="font-bold">{currentIdx + 1}</p>/{questions.length}</p>
                    <button className="text-2xl pr-4" onClick={handleReset}><Icon icon="ri:reset-left-fill" /></button>
                </div>
            </header>
            <div className="flex gap-5 pb-4">
                <nav className="w-1/5">
                    <div className="flex flex-col gap-2 justify-center items-center">
                        {!state.isFirst && <Circle width="7" onClick={() => navigation.prev()}>{Number(state.current.data.id) - 1}</Circle>}
                        <Circle width="10">{Number(state.current.data.id)}</Circle>
                        {!state.isLast && <Circle width="7" onClick={() => navigation.next()}>{Number(state.current.data.id) + 1}</Circle>}
                    </div>
                </nav>
                <main className="w-3/5 bg-white h-full rounded-2xl shadow p-2">
                    <SwitchCard>
                        <SwitchCard.Primary>
                            <div className="">
                                {state.current.data.question}
                            </div>
                        </SwitchCard.Primary>
                        <SwitchCard.Secondary>
                            <div className="flex flex-col gap-4">
                                <p>{state.current.data.answer}</p>
                                <div className="flex justify-evenly">
                                    <Button type="Info" onClick={() => onAnswer('CORRECT')} label="Ok" />
                                    <Button type="Error" onClick={() => onAnswer('WRONG')} label="Bad" />
                                </div>
                            </div>
                        </SwitchCard.Secondary>
                    </SwitchCard>
                </main>
                <aside className="w-1/5 bg-sky-900 p-3 rounded-lg flex flex-col gap-4">
                    <div className="text-sky-50 text-2xl flex gap-1">{Array.from({ length: state.current.data.level }, () => <Icon icon={'streamline-flex:skull-2-remix'} />)}</div>
                    <div className="flex gap-2">{state.current.data.tags.map((t) =>
                        <p className="bg-sky-100 rounded-full px-2 text-sm">{t}</p>)}</div>
                </aside>
            </div>
        </div>
    </>
    )
}
