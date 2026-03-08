import { Listbox, ListboxButton, ListboxOptions, ListboxOption, Field, Label } from "@headlessui/react";
import { useFormContext, useController } from "react-hook-form";
import { OptionSelect, OptionSelectProps } from "../OptionSelect/OptionSelect";

export type LanguageSelectorProps = OptionSelectProps & {
    fieldName: string
}

export const FormOptionSelector = ({ options, fieldName, placeholder }: LanguageSelectorProps) => {
    const defaultPlaceholder = placeholder ?? 'Select'
    const form = useFormContext();
    const { field } = useController({ control: form.control, name: fieldName })

    return (<>
        <OptionSelect
            value={field.value}
            onChange={field.onChange}
            options={options} />
    </>)
}