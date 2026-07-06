import { Dispatch, SetStateAction } from "react"

export type FormInputErrors = {
    errors: string[],
    properties: {
        [key: string]: {
            errors: string[]
        }
    }
}

export type FormInputType = {
    errors: FormInputErrors,
    inputKey: string,
    form: any,
    setForm: Dispatch<SetStateAction<any>>
}
