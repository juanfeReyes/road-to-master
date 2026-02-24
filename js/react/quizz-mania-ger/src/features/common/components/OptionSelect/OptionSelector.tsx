import { Listbox, ListboxButton, ListboxOptions, ListboxOption, Field, Label } from "@headlessui/react";
import { useFormContext, useController } from "react-hook-form";

export type LanguageSelectorProps = {
    placeholder?: string,
    readonly options: readonly String[],
    fieldName: string
}

export const OptionSelector = ({ options, fieldName, placeholder }: LanguageSelectorProps) => {
    const defaultPlaceholder = placeholder ?? 'Select'
    const form = useFormContext();
    const { field } = useController({ control: form.control, name: fieldName })

    return (<>
        <Listbox value={field.value} onChange={field.onChange}>
            <ListboxButton className={"border-b-2 px-1 border-gray-200 w-full text-left"}>{field.value ?? defaultPlaceholder}</ListboxButton>
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