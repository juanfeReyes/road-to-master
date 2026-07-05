import { Dispatch, SetStateAction } from "react"

export type FormInputType = {
    inputKey: string,
    form: any,
    setForm: Dispatch<SetStateAction<any>>
}
