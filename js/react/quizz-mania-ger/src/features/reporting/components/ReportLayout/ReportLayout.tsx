'use client'

import { useGameStore } from "@/src/features/common/store/GameStore"
import {Tab, TabGroup, TabList, TabPanel, TabPanels } from "@headlessui/react"
import { Icon } from "@iconify/react"

import { useRouter, useSearchParams } from "next/navigation"
import { GeneralReport } from "../GeneralReport/GeneralReport"
import LabeledPieChart from "@/src/features/common/components/PieChart/PieChart"

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

    const router = useRouter()
    if (!game || !game.report || !game.report.answers || !game.report.answers) {
        router.push("/")
        return;
    }
    const wrong = game.report.answers.filter(a => a.result === 'WRONG');
    const correct = game.report.answers.filter(a => a.result === 'CORRECT');;
    const scorePercentage = (correct.length / (correct.length + wrong.length)) * 100

    const passedByDomain = Object.entries(Object.groupBy(correct, (q) => q.domainName)).map(([key, val]) => ({name: key, value: val.length}))
    const failedByDomain = Object.entries(Object.groupBy(wrong, (q) => q.domainName)).map(([key, val]) => ({name: key, value: val.length}))


    const tabStyle = 'data-hover:bg-sky-200 data-selected:bg-sky-50 pt-1.5 px-1.5 rounded-t-xl'

    return (<div className="flex flex-col p-4 gap-1 rounded-l-lg shadow-2xl bg-gray-100">
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
                        <div className="flex">
                            <LabeledPieChart data={passedByDomain} />
                            <LabeledPieChart data={failedByDomain} />
                        </div>
                    </TabPanel>
                </TabPanels>
            </TabGroup>

        </div>

    </div>)
}
