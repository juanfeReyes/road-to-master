'use client'
import { ReactNode, useState, DragEvent } from "react"

type Group = {
    title: string | ReactNode,
    data: any[]
}

type DragAndDropProps = {
    groups: any[],
    data: Record<string, any>[],
    groupBy: (val: any, group: string) => boolean,
    handleUpdateGroup: (val: any, group: String) => void
    card: (val: any) => ReactNode
}

const initialDragItem = { data: {} };

export const DragAndDrop = ({ groups, data, groupBy, handleUpdateGroup, card }: DragAndDropProps) => {
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

    return (<div className="flex gap-2 h-full">
        {
            groups.map((g, gidx) => (
                <div
                    key={gidx}
                    className="h-full"
                    onDragEnter={(e) => handleDragEnter(e, g)}
                    onDragOver={(e) => e.preventDefault()}
                    onDrop={(e) => handleOnDrop(e)}
                >
                    <h1>{g}</h1>
                    <div className="h-full">
                        {data
                            .filter(d => groupBy(d, g))
                            .map((gd, didx) => (
                                <div
                                    id={gd.id}
                                    key={didx}
                                    draggable
                                    onDragStart={(e) => handleDragStart(e)}
                                    onDragOver={(e) => e.preventDefault()}
                                    // onDragEnd={() => handleOnDragEnd()}
                                >
                                    {card(gd)}
                                </div>
                            ))}
                    </div>
                </div>
            ))
        }
    </div>)
}
