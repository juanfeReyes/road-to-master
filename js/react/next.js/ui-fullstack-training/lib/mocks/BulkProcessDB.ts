import { BulkProcessSchema } from "@/types/BulkProcess";
import { Collection } from "@msw/data";

export const BulkProcessDB = new Collection({
    schema: BulkProcessSchema
})
