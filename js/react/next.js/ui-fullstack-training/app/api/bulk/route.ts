import { BulkProcessDB } from "@/lib/mocks/BulkProcessDB"
import { NextResponse } from "next/server"

export const GET = async () => {
    const bulkProcesses = BulkProcessDB.all()
    return NextResponse.json(bulkProcesses, {status: 200})
}
