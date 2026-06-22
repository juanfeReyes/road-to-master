import { ReactNode } from "react";

export type TableHeader = {
    name: string,
    label: string,
    cell?: (val: any) => ReactNode
    header?: (val: any) => ReactNode
}

type TableProps = {
    headers: TableHeader[]
    data: Record<string, any>[]
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
        <>
            {cellValue}
        </>
    )
};

export const Table = ({ data, headers }: TableProps) => {
    if (data.length === 0) {
        return ('No data provided')
    }

    return (
        <div className={`grid grid-cols-${headers.length}`}>
            {headers.map((header, idx) => (<div key={header.name} className={`col-start-${idx + 1}`} >{header.label}</div>))}
            {data.map(row =>
            (headers.map((header, idx) => (<div key={row.id} className={`col-start-${idx + 1}`}>
                <TableCell row={row} header={header} />
            </div>)))
            )}
        </div>
    )
}
