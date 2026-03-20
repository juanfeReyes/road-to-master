import { Button } from "@/src/features/common/components/Button/Button"
import { useGameStore } from "@/src/features/common/store/GameStore"
import { StepperNavigation } from "@stepperize/core"
import { useRouter } from "next/navigation"


type QuizGenResumeProps = {
    onClose: () => void,
    navigation: StepperNavigation<any>
}

export const QuizGenResume = ({ navigation, onClose }: QuizGenResumeProps) => {
    const router = useRouter()
    const { currentGame, startGame } = useGameStore()
    const setup = currentGame?.setup ?? null

    const handleOnBack = () => {
        navigation.prev()
    }

    const handleOnStart = () => {
        startGame()
        onClose()
        router.push(`/game?id=${currentGame?.id}`)
    }

    return (<div className="flex flex-col gap-4">
        {setup && <>
            {setup.timer?.minutes && <div>{setup.timer.minutes} Minutes</div>}
            <div className="flex justify-end py-2 pr-4 gap-1.5">
                <p>Total questions</p> <p className="font-black">{setup.totalQuestions}</p>
            </div>
            <table className="w-full border-separate">
                <thead>
                    <tr className="bg-sky-900 font-bold text-slate-100 border-separate">
                        <th className="rounded-l-2xl">{setup.sortBy}</th>
                        <th>Total questions</th>
                        <th className="rounded-r-2xl">Domain %</th>
                    </tr>
                </thead>
                <tbody>
                    {Object.entries(setup.questionsGrouped)
                        .map(([key, value]) => (<tr id="key" className="text-center hover:bg-slate-100">
                            <td className="text-left pl-3 rounded-l-lg">{key}</td>
                            <td>{value?.length}</td>
                            <td className="rounded-r-lg">{((value?.length / setup.totalQuestions) * 100).toFixed(2)}%</td>
                        </tr>))}
                </tbody>
            </table>
        </>
        }
        <div className="flex justify-evenly">
            <Button label="Back" type="Neutro" onClick={handleOnBack} />
            <Button label="Start Game" type="Info" onClick={handleOnStart} />
        </div>
    </div>)
}
