import { Listbox, ListboxButton, ListboxOptions, ListboxOption } from "@headlessui/react"
import { ComponentProps } from "react"

export type OptionSelectProps  = ComponentProps<'select'> & {
    placeholder?: string,
    readonly options: readonly String[],
}
export const OptionSelect = ({value, onChange, options, placeholder}:  OptionSelectProps) => {
    const defaultPlaceholder = placeholder ?? 'Select'
        
        return (<>
            <Listbox value={value} onChange={onChange}>
                <ListboxButton className={"border-b-2 px-1 border-gray-200 w-full text-left"}>{value ?? defaultPlaceholder}</ListboxButton>
                <ListboxOptions className={"bg-gray-200 p-1.5 rounded-b-xl w-(--button-width)"} anchor="bottom">
                    {options.map((opt, idx) => (
                        <ListboxOption key={idx} value={opt} className="data-focus:bg-blue-200 rounded-lg p-1">
                            {opt}
                        </ListboxOption>
                    ))}
                </ListboxOptions>
            </Listbox>
        </>)
}
