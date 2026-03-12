import { Table } from "@/src/features/common/components/Table/Table";
import { Question, QuestionResult, QuestionState } from "@/src/features/common/model/Question";
import { Game } from "@/src/features/common/store/GameStore";
import { Icon } from "@iconify/react";
import { createColumnHelper } from "@tanstack/react-table";
import router from "next/router";

interface QuestionRowPanelProps {
    question: Question
}

const QuestionRowPanel = ({ question }: QuestionRowPanelProps) => {

    return (<div className="pl-8 py-2">
        {question.answer}
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

type GeneralReportProps = {
    game: Game
}
export const GeneralReport = ({ game }: GeneralReportProps) => {
    if (!game || !game.report || !game.report.answers) {
        router.push("/")
        return;
    }

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
                optionDisplay: (option) => <ResultIcon result={option} />
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

    return (<div>
        <Table data={game.report.answers} columns={columns} rowExpandPanel={(row) => <QuestionRowPanel question={row} />} />
    </div>)
}
