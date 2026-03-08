import { Icon } from "@iconify/react"
import { Column, ColumnDef, ColumnFiltersState, ExpandedState, flexRender, getCoreRowModel, getExpandedRowModel, getFacetedRowModel, getFacetedUniqueValues, getFilteredRowModel, getSortedRowModel, Header, RowData, SortingState, useReactTable } from "@tanstack/react-table"
import { useState } from "react"
import { OptionSelect } from "../OptionSelect/OptionSelect"
import { Popover, PopoverButton, PopoverPanel } from "@headlessui/react"

interface TableProps<TData extends RowData> {
    data: TData[],
    columns: ColumnDef<TData, any>[],
    rowExpandPanel?: (originalRow: TData) => any
}

type SortingIconProps = {
    column: Column<any, any>
}
const SortingIcon = ({ column }: SortingIconProps) => {
    switch (column.getNextSortingOrder()) {
        case 'asc':
            return (<Icon icon={'iconamoon:arrow-down-2-fill'} />)
        case 'desc':
            return (<Icon icon={'wordpress:line-solid'} />)
        default:
            return (<Icon icon={'iconamoon:arrow-up-2-fill'} />)
    }

}

type FilterProps = {
    header: Header<any, unknown>
}
const Filter = ({ header }: FilterProps) => {
    const { filterVariant } = header.column.columnDef.meta ?? {}
    const columnFilterValue = header.column.getFilterValue()
    let filterComponent;
    switch (filterVariant) {
        case 'range':
            filterComponent = (<Icon icon={'iconamoon:arrow-down-2-fill'} />)
            break;
        case 'select':
            const sortedUniqueValues = Array.from(header.column.getFacetedUniqueValues().keys())
                .sort()
                .slice(0, 5000)
            filterComponent = (<OptionSelect
                value={columnFilterValue}
                onChange={(e) => header.column.setFilterValue(e)}
                options={sortedUniqueValues}
            />)
            break;
        default:
            filterComponent = (<input value={columnFilterValue} onChange={(e) => header.column.setFilterValue(e.target.value)} />)
    }

    return (
        <Popover className="relative">
            <PopoverButton as="button" className={'w-full block'}>{flexRender(header.column.columnDef.header, header.getContext())}</PopoverButton>
            <PopoverPanel anchor="bottom" className="flex flex-col bg-white">
                {filterComponent}
            </PopoverPanel>
        </Popover>
    )
}

export const Table = <T,>({ data, columns, rowExpandPanel }: TableProps<T>) => {
    const [expanded, setExpanded] = useState<ExpandedState>({})
    const [sorting, setSorting] = useState<SortingState>([])
    const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([])

    const table = useReactTable({
        data,
        columns,
        state: {
            expanded,
            sorting,
            columnFilters
        },
        getCoreRowModel: getCoreRowModel(),
        getExpandedRowModel: getExpandedRowModel(),
        getSortedRowModel: getSortedRowModel(),
        getFilteredRowModel: getFilteredRowModel(),
        getFacetedRowModel: getFacetedRowModel(),
        getFacetedUniqueValues: getFacetedUniqueValues(),
        onExpandedChange: setExpanded,
        onSortingChange: setSorting,
        onColumnFiltersChange: setColumnFilters,
        getRowCanExpand: (row) => true,
        // debugTable: true,
    })

    return (<div>
        <table className="w-full">
            <thead>
                {table.getHeaderGroups().map(headerGroup => <tr key={headerGroup.id}>
                    {headerGroup.headers.map(header => <th key={header.id}>
                        <div className="w-full flex gap-2">
                            {header.column.getCanSort() && <button onClick={header.column.getToggleSortingHandler()}><SortingIcon column={header.column} /></button>}
                            {header.column.getCanFilter() ?
                                <Filter header={header} /> :
                                flexRender(header.column.columnDef.header, header.getContext())}
                        </div>
                    </th>)}
                </tr>)}
            </thead>
            <tbody>
                {table.getRowModel().rows.map(row => <>
                    <tr key={row.id}>
                        {row.getVisibleCells().map(cell => <td key={cell.id}>
                            {flexRender(cell.column.columnDef.cell, cell.getContext())}
                        </td>)}
                    </tr>
                    {row.getIsExpanded() && (
                        <tr>
                            <td colSpan={row.getAllCells().length}>
                                {rowExpandPanel && rowExpandPanel(row.original)}
                            </td>
                        </tr>
                    )}
                </>
                )}
            </tbody>
        </table>
    </div>)
}
