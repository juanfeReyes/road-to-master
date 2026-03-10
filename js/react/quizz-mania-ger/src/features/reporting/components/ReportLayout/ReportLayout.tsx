'use client'
import { Table } from "@/src/features/common/components/Table/Table"
import { GameQuestion, Question, QuestionResult, QuestionState } from "@/src/features/common/model/Question"
import { useGameStore } from "@/src/features/common/store/GameStore"
import { Disclosure, DisclosureButton, DisclosurePanel, Tab, TabGroup, TabList, TabPanel, TabPanels } from "@headlessui/react"
import { Icon } from "@iconify/react"
import { createColumnHelper } from "@tanstack/react-table"
import clsx from "clsx"
import { useRouter, useSearchParams } from "next/navigation"

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

interface QuestionRowPanelProps {
    question: Question
}

const QuestionRowPanel = ({ question }: QuestionRowPanelProps) => {

    return (<div className="pl-8 py-2">
        {question.answer}
    </div>)
}

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

type ResultIconProps = {
    result: QuestionState
}
const ResultIcon = ({result}: ResultIconProps) => {

    return (<div className="flex justify-center">
        <p className={`${result === 'CORRECT' ? 'bg-green-400' : 'bg-red-400'} rounded-full p-1`}>
            {result === 'CORRECT' ? <Icon icon={'el:ok-sign'} /> : <Icon icon={'mdi:cross-circle'} />}
        </p>
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

    const columnHelper = createColumnHelper<QuestionResult>();

    const columns = [
        columnHelper.display({
            id: 'expand',
            header: () => (<p>-</p>),
            cell: ({ row }) => (
                <button onClick={row.getToggleExpandedHandler()}>
                    {row.getIsExpanded() ? <Icon icon={'prime:sort-down'} /> : <Icon icon={'icon-park-solid:right-one'} />}
                </button>
            )
        }),
        columnHelper.accessor("question", {
            header: "question",
            sortingFn: 'alphanumeric',
            cell: (info) => info.getValue(),
        }),
        columnHelper.accessor("result", {
            header: "result",
            sortingFn: 'alphanumeric',
            cell: (info) => <ResultIcon result={info.getValue()} />,
            meta: {
                filterVariant: 'select',
                optionDisplay: (option) =>  <ResultIcon result={option} />
            }
        }),
        columnHelper.accessor("domainName", {
            header: "domain",
            sortingFn: 'alphanumeric',
            cell: (info) => <span className="flex justify-center">{info.getValue()}</span>,
        }),
        columnHelper.accessor("role", {
            header: "role",
            sortingFn: 'alphanumeric',
            cell: (info) => <span className="flex justify-center">{info.getValue()}</span>,
            meta: {
                filterVariant: 'select'
            }
        }),
        columnHelper.accessor("level", {
            header: "level",
            sortingFn: 'alphanumeric',
            cell: (info) => <span className="flex justify-center">{info.getValue()}</span>,
            meta: {
                filterVariant: 'select'
            }
        }),
    ];

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
                        <Table data={game.report.answers} columns={columns} rowExpandPanel={(row) => <QuestionRowPanel question={row} />} />
                    </TabPanel>
                    <TabPanel>Content 2</TabPanel>
                </TabPanels>
            </TabGroup>

        </div>

    </div>)
}
