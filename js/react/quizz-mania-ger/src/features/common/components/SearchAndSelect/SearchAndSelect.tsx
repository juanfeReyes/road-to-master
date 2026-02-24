import { Combobox, ComboboxInput, ComboboxOptions, ComboboxOption, Field, Label } from "@headlessui/react"
import { useState } from "react"
import { useFormContext, useController } from "react-hook-form"

export interface SearchAndSelectOption {
    name: string,
    [key: string]: any
}

export type SearchAndSelectProps = {
    placeholder?: string,
    fieldName: string,
    options: SearchAndSelectOption[],
    filterBy: (option: SearchAndSelectOption, query: string) => boolean
}
/**
 * Create search and select component
 * @returns 
 */
export const SearchAndSelect = ({ options, filterBy, fieldName, placeholder }: SearchAndSelectProps) => {
    const defaultPlaceholder = placeholder ?? 'Type and select'
    const { control, setValue } = useFormContext()
    const { field } = useController({ control: control, name: fieldName })

    const [query, setQuery] = useState('')

    const handleCleanOption = (option: string) => {
        const options = field.value.filter((opt: { name: string }) => opt.name !== option)
        setValue("quizzes", options)
    }

    const filteredOption =
        query === ''
            ? options
            : options.filter((opt) => filterBy(opt, query))


    return (<>
        <Combobox multiple value={field.value} onChange={field.onChange} onClose={() => setQuery('')}>
            {field.value.length > 0 && (
                <ul className="flex gap-1.5 pb-1">
                    {field.value.map((opt, idx) => (<li
                        key={idx}
                        className="bg-blue-300 rounded-xl px-1"
                        onClick={(event) => handleCleanOption(event.target.textContent)}
                        id={idx}>{opt.name}</li>))}
                </ul>
            )}
            <ComboboxInput
                placeholder={defaultPlaceholder}
                className={"border-b-2 border-gray-200 placeholder-black w-full"}
                aria-label="Assignee"
                displayValue={(option) => option?.name}
                onChange={(event) => setQuery(event.target.value)}
            />
            <ComboboxOptions anchor="bottom start" className="empty:invisible bg-gray-200 p-1.5 rounded-b-xl w-(--input-width)">
                {filteredOption.map((option, idx) => (
                    <ComboboxOption key={idx} value={option} className="data-focus:bg-blue-100">
                        {option.name}
                    </ComboboxOption>
                ))}
            </ComboboxOptions>
        </Combobox>
    </>)
}