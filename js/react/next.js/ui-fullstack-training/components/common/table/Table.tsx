
import { ReactNode } from "react";
import { CustomDialog, CustomDialogProps } from "../CustomDialog";

export type TableHeader = {
    name: string,
    label: string,
    cell?: (val: any) => ReactNode
    header?: (val: any) => ReactNode
}


type TableHeaderProps = {
    header: TableHeader
};
const TableHeader = ({ header }: TableHeaderProps) => {
    const cellValue = header.label
    if (cellValue == undefined) {
        return <></>
    }

    if (header.header !== undefined) {
        return (
            <>
                {header.header(cellValue)}
            </>
        )
    }

    return (
        <>
            {cellValue}
        </>
    )
};


type TableCellProps = {
    row: Record<string, any>,
    header: TableHeader
};
const TableCell = ({ row, header }: TableCellProps) => {
    const cellValue = row[header.name]
    if (cellValue == undefined) {
        return <></>
    }

    if (header.cell !== undefined) {
        return (
            <>
                {header.cell(cellValue)}
            </>
        )
    }

    return (
        <p>
            {cellValue}
        </p>
    )
};

const colStyle = {
    2: 'grid-cols-2',
    3: 'grid-cols-3',
    4: 'grid-cols-4',
    5: 'grid-cols-5',
    6: 'grid-cols-6',
    7: 'grid-cols-7',
};

type TableProps = {
    headers: TableHeader[]
    data: Record<string, any>[],
    headerContent: ReactNode
};

export const Table = ({ data, headers, add, headerContent }: TableProps) => {
    if (data.length === 0) {
        return ('No data provided')
    }

    return (<div className="p-3 flex flex-col gap-2">
        <div className="flex justify-end">
            {headerContent && headerContent}
        </div>
        <div className={`grid ${colStyle[headers.length]}`}>
            {headers.map((header, idx) => (<div key={header.name}
                className={`col-start-${idx + 1} font-bold text-center m-1 my-5 rounded-md bg-white`} >
                <TableHeader header={header} />
            </div>))}
            {data.map(row =>
            (headers.map((header, idx) => (<div key={`${row.id}-${idx}`}
                className={`col-start-${idx + 1} bg-white m-1 rounded-md flex items-center px-2`}>
                <TableCell row={row} header={header} />
            </div>)))
            )}
        </div>
    </div>
    )
}
