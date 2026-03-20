'use client'

import { useGameStore } from "@/src/features/common/store/GameStore"
import { Tab, TabGroup, TabList, TabPanel, TabPanels } from "@headlessui/react"
import { Icon } from "@iconify/react"

import { useRouter, useSearchParams } from "next/navigation"
import { GeneralReport } from "../GeneralReport/GeneralReport"
import { ChartsReport } from "../ChartsReport/ChartsReport"
import { usePDF } from "react-to-pdf"

type ScoreCardProps = {
    score: string | number,
    label: string,
    icon: string,
}
const ScoreCard = ({ score, label, icon }: ScoreCardProps) => {

    return (<div className="flex gap-4 justify-center items-center">
        <Icon className="text-4xl text-slate-600" icon={icon} />
        <div className="flex flex-col gap-1">
            <p className="font-medium text-xs">{label}</p>
            <p className="font-semibold text-2xl flex justify-center">{score}</p>
        </div>
    </div>)
}


export const ReportLayout = () => {
    const searchParams = useSearchParams()
    const game = useGameStore((s) => s.lastGames.find((g) => g.id === searchParams.get('id')))
    const { resetGame, currentGame } = useGameStore();
    const { toPDF, targetRef } = usePDF({ filename: 'page.pdf' });

    const router = useRouter()
    if (!game || !game.report || !game.report.answers || !game.report.answers) {
        router.push("/")
        return <div></div>;
    }
    const wrong = game.report.answers.filter(a => a.result === 'WRONG');
    const correct = game.report.answers.filter(a => a.result === 'CORRECT');
    const scorePercentage = (correct.length / (correct.length + wrong.length)) * 100

    const tabStyle = 'data-hover:bg-sky-200 data-selected:bg-sky-50 pt-1.5 px-1.5 rounded-t-xl'

    const onCompleteGame = () => {
        resetGame()
        router.push("/")
    }

    return (<div className="flex flex-col p-4 gap-1">
        <div className="flex justify-end gap-2 text-3xl bg-teal-100 rounded-xl px-4 py-1">
            <button
                className="hover:bg-teal-200 rounded-full p-1   transition-all duration-75 active:scale-95 shadow-md active:shadow-sm"
                onClick={() => toPDF()}><Icon icon={'fa7-solid:file-pdf'} />
            </button>
            {currentGame &&
                <button className="hover:bg-teal-200 rounded-full p-1   transition-all duration-75 active:scale-95 shadow-md active:shadow-sm"
                    onClick={onCompleteGame}>
                    <Icon icon={'pajamas:go-back'} />
                </button>
            }
        </div>
        <div ref={targetRef} className="flex flex-col py-4 gap-1 rounded-l-lg shadow-2xl bg-gray-100">
            <div className="font-bold text-3xl py-2 ">Game Report - {game.report.startDate.toLocaleString()}</div>
            <div className="flex justify-evenly py-4">
                <ScoreCard label="correct answers" score={correct.length} icon="el:ok-sign" />
                <ScoreCard label="wrong answers" score={wrong.length} icon="mdi:cross-circle" />
                <ScoreCard label="score" score={scorePercentage.toFixed(1)} icon="mynaui:percentage-hexagon-solid" />
                <ScoreCard label="total " score={game.report.answers.length} icon="fluent:book-number-24-filled" />
            </div>
            <div>
                <TabGroup className={'bg-sky-50 rounded-xl pb-2 shadow'}>
                    <TabList className={'bg-sky-500 rounded-t-xl flex gap-1'}>
                        <Tab className={tabStyle}>Questions</Tab>
                        <Tab className={tabStyle}>Charts</Tab>
                    </TabList>
                    <TabPanels className={' rounded-xl'}>
                        <TabPanel>
                            <GeneralReport game={game} />
                        </TabPanel>
                        <TabPanel>
                            <ChartsReport game={game} />
                        </TabPanel>
                    </TabPanels>
                </TabGroup>

            </div>

        </div>
    </div>
    )
}
