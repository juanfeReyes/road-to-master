'use client'
import { Question } from "@/src/features/common/model/Question"
import { Disclosure, DisclosureButton, DisclosurePanel } from "@headlessui/react"
import { Icon } from "@iconify/react"
import clsx from "clsx"
import { useGameStore } from "../store/GameStore"
import { useRouter } from "next/navigation"

interface QuestionReportProps {
    question: Question
}

const AnswerReport = ({ question }: QuestionReportProps) => {

    return (<div className="flex flex-col gap-1 p-3">
        <div className="flex gap-1">{question.tags.map(tag => <p>{tag}</p>)}</div>
        <div className="font-bold">{question.question}</div>
        <div className="p-2">{question.answer}</div>
    </div>)

}

interface AnswerReportPanelProps {
    title: string,
    color: string,
    answers: Question[]
}

const AnswerReportPanel = ({ answers, title, color }: AnswerReportPanelProps) => {

    return (<>
        {answers.length > 0 && <Disclosure>
            {({ open }) => (
                <>
                    <DisclosureButton className={`items-center flex justify-between ${color} p-1 rounded-md`}>
                        <span>{title}</span>
                        <Icon icon="bx:up-arrow" className={clsx(open && 'rotate-180')} />
                    </DisclosureButton>
                    <DisclosurePanel >
                        <div className="inset-shadow-lg">
                            {answers.map(ans => <AnswerReport question={ans} />)}
                        </div>
                    </DisclosurePanel>
                </>
            )}

        </Disclosure>}
    </>)
}

interface GameReportProps {
    correctAnswers: Question[]
    wrongAnswers: Question[]
}

export const GameReport = () => {
    const { currentGame } = useGameStore()
    const router = useRouter()
    if (currentGame === null) {
        router.push("/")
        return;
    }
    const wrong = currentGame.report.wrong;
    const correct = currentGame.report.correct;
    console.log(currentGame)
    const scorePercentage = (correct.length / (correct.length + wrong.length)) * 100

    return (<div className="flex flex-col p-4 gap-1 rounded-l-lg shadow-2xl bg-gray-100">
        <div className="font-bold text-3xl py-2 ">Resultados</div>
        <div>Puntaje: <span className="text-xl">{correct.length}</span>/<span className="text-lg">{correct.length + wrong.length}</span> - ({scorePercentage.toFixed(2)}%)</div>
        <AnswerReportPanel title={`Respuestas Erroneas: ${wrong.length}`} answers={wrong} color="bg-red-300" />
        <AnswerReportPanel title={`Respuestas correctas: ${correct.length}`} answers={correct} color="bg-green-300" />
    </div>)
}
