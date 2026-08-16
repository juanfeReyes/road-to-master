import * as z from "zod"

const BulkProcessType = {
    Task: 'Task'
} as const;

export type BulkProcessType = typeof BulkProcessType[keyof typeof BulkProcessType];

export type BulkProcess = {
    id?: string,
    type: BulkProcessType,
    file: File,
    startDateTime: Date,
    completeDateTime: Date
}


export const BulkProcessSchema = z.object({
    id: z.uuid().optional(),
    type: z.custom<BulkProcessType>(),
    file: z.custom<File>(),
    startDateTime: z.date().or(z.string()),
    completeDateTime: z.date().or(z.string()),
})
