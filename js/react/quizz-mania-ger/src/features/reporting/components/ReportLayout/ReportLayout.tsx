'use client'
import { Table } from "@/src/features/common/components/Table/Table"
import { GameQuestion, Question, QuestionResult } from "@/src/features/common/model/Question"
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

const QuestionsList = () => {

    return (<div>

    </div>)
}

const QuestionsCharts = () => {
    return (<div>

    </div>)
}

interface QuestionRowPanelProps {
    question: Question
}

const QuestionRowPanel = ({question}: QuestionRowPanelProps) => {

    return (<div>
        {question.answer}
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
            cell: ({row}) => (
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
            cell: (info) => <span className={`${info.getValue() === 'CORRECT' ? 'bg-green-400' : 'bg-red-400'} rounded-xl px-2 py-0.5`}>
                {info.getValue()}
                </span>,
            meta: {
                filterVariant: 'select'
            }
        }),
        columnHelper.accessor("domainName", {
            header: "domain",
            sortingFn: 'alphanumeric',
            cell: (info) => info.getValue(),
        }),
        columnHelper.accessor("role", {
            header: "role",
            sortingFn: 'alphanumeric',
            cell: (info) => info.getValue(),
            meta: {
                filterVariant: 'select'
            }
        }),
        columnHelper.accessor("level", {
            header: "level",
            sortingFn: 'alphanumeric',
            cell: (info) => info.getValue(),
            meta: {
                filterVariant: 'select'
            }
        }),
    ];

    const tabStyle = 'data-hover:bg-sky-200 data-selected:bg-sky-50 pt-1.5 px-1.5 rounded-t-xl'

    return (<div className="flex flex-col p-4 gap-1 rounded-l-lg shadow-2xl bg-gray-100">
        <div className="font-bold text-3xl py-2 ">Report</div>
        <div>Score: <span className="text-xl">{correct.length}</span>/<span className="text-lg">{correct.length + wrong.length}</span> - ({scorePercentage.toFixed(2)}%)</div>
        <div>
            <TabGroup className={'bg-sky-50 rounded-xl pb-2 shadow'}>
                <TabList className={'bg-sky-500 rounded-t-xl flex gap-1'}>
                    <Tab className={tabStyle}>Questions</Tab>
                    <Tab className={tabStyle}>Charts</Tab>
                </TabList>
                <TabPanels className={' rounded-xl'}>
                    <TabPanel>
                        <Table data={game.report.answers} columns={columns} rowExpandPanel={(row) => <QuestionRowPanel question={row} /> }/>
                    </TabPanel>
                    <TabPanel>Content 2</TabPanel>
                </TabPanels>
            </TabGroup>

        </div>

    </div>)
}
