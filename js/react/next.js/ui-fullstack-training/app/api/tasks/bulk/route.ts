import { BulkProcessDB } from "@/lib/mocks/BulkProcessDB"
import { tasksDB } from "@/lib/mocks/TasksDB"
import { NextRequest, NextResponse } from "next/server"
import * as XLSX from 'xlsx'

export const POST = async (request: NextRequest) => {
    const formData = await request.formData()

    const file = formData.get('file') as File
    const fileBuffer = Buffer.from(await file.arrayBuffer())
    const workbook = XLSX.read(fileBuffer, { type: 'buffer' })
    const sheetName = workbook.SheetNames[0]
    const workSheet = workbook.Sheets[sheetName]

    const data = XLSX.utils.sheet_to_json(workSheet)

    const starDate = new Date()
    for (const item of data) {
        const task = {...item, id: crypto.randomUUID(), files: []}
        await tasksDB.create(task)
    }
    await BulkProcessDB.create({
        type: 'Task',
        file: file,
        startDateTime: starDate,
        completeDateTime: new Date(),
        id: crypto.randomUUID()
    })

    return NextResponse.json({})
}
