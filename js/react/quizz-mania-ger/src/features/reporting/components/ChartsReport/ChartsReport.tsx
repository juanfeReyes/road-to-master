import LabeledPieChart from "@/src/features/common/components/PieChart/PieChart";
import { Game } from "@/src/features/common/store/GameStore"


type ChartsReportProps = {
    game: Game
}
export const ChartsReport = ({ game }: ChartsReportProps) => {
    if (!game || !game.report || !game.report.answers) {
        return <div></div>;
    }
    const wrong = game.report.answers.filter(a => a.result === 'WRONG');
    const correct = game.report.answers.filter(a => a.result === 'CORRECT');

    const passedByDomain = Object.entries(Object.groupBy(correct, (q) => q.domainName)).map(([key, val]) => ({ name: key, value: val.length }))
    const failedByDomain = Object.entries(Object.groupBy(wrong, (q) => q.domainName)).map(([key, val]) => ({ name: key, value: val.length }))

    return (
        <div className="flex">
            <div className="flex flex-col w-full">
                <h3 className="pl-4 pt-2 text-2xl font-bold">Results by domain</h3>
                <div className="flex">
                    <LabeledPieChart data={passedByDomain} />
                    <LabeledPieChart data={failedByDomain} />
                </div>
            </div>
        </div>
    )
}

