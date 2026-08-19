'use client'
import { ReactNode, useState, DragEvent } from "react"
import { CustomDialog, CustomDialogProps } from "../layout/CustomDialog"

type Group = {
    title: string | ReactNode,
    data: any[]
}

type DragAndDropProps = {
    groups: any[],
    data: Record<string, any>[],
    groupBy: (val: any, group: string) => boolean,
    handleUpdateGroup: (val: any, group: String) => void
    card: (val: any) => ReactNode,
    add?: CustomDialogProps
}

const colStyle = {
    2: 'grid-cols-2',
    3: 'grid-cols-3',
    4: 'grid-cols-4',
    5: 'grid-cols-5',
    6: 'grid-cols-6',
    7: 'grid-cols-7',
};

export const DragAndDrop = ({ groups, data, add, groupBy, handleUpdateGroup, card }: DragAndDropProps) => {
    const [dragItem, setDragItem] = useState()
    const [swimlane, setSwimlane] = useState('')

    const handleOnDrop = (e: DragEvent<HTMLDivElement>) => {
        e.preventDefault()
        console.log('drop', swimlane)
        const value = data[data.findIndex(d => d.id === dragItem.id)]
        if (value['status'] !== swimlane) {
            handleUpdateGroup(value, swimlane)
            setDragItem(null)
        }
    }

    const handleDragEnter = (e: DragEvent<HTMLDivElement>, newGroup: string) => {
        console.log('enter', newGroup)
        e.preventDefault()
        setSwimlane(newGroup)
    }

    const handleDragStart = (e: DragEvent<HTMLDivElement>) => {
        e.dataTransfer.effectAllowed = 'move'
        setDragItem(e.target)
    }

    return (
        <div className="p-3 flex flex-col gap-2">
            <div className="flex justify-end">
                {add && <CustomDialog {...add} />}
            </div>
            <div className={`grid ${colStyle[groups.length]} place-items-center gap-6 h-full px-4`}>
                {
                    groups.map((g, gidx) => (
                        <div
                            key={gidx}
                            className="h-full w-full bg-gray-100 rounded-2xl shadow-inner shadow-gray-500"
                            onDragEnter={(e) => handleDragEnter(e, g)}
                            onDragOver={(e) => e.preventDefault()}
                            onDrop={(e) => handleOnDrop(e)}
                        >
                            <h1 className="text-center mb-5 py-2 font-bold bg-white rounded-t-2xl">{g}</h1>
                            <div className="h-full flex flex-col items-center gap-2">
                                {data
                                    .filter(d => groupBy(d, g))
                                    .map((gd, didx) => (
                                        <div
                                            id={gd.id}
                                            key={didx}
                                            draggable
                                            onDragStart={(e) => handleDragStart(e)}
                                            onDragOver={(e) => e.preventDefault()}
                                        >
                                            {card(gd)}
                                        </div>
                                    ))}
                            </div>
                        </div>
                    ))
                }
            </div>
        </div>)
}
