'use client'

import { OptionSelector } from "@/src/features/common/components/OptionSelect/OptionSelector"
import { SearchAndSelect } from "@/src/features/common/components/SearchAndSelect/SearchAndSelect"
import { useGameStore } from "@/src/features/common/store/GameStore"
import { Field, Disclosure, DisclosureButton, DisclosurePanel } from "@headlessui/react"
import { FormProvider, useForm, useWatch } from "react-hook-form"
import { Icon } from "@iconify/react"
import { useQuizGenForm } from "../../hooks/useQuizGenForm"
import { Button } from "@/src/features/common/components/Button/Button"
import { maxQaOptions, sortOptionLabels, timerOptions } from "@/src/features/common/model/Options.js"
import { StepperNavigation } from "@stepperize/core"

type QuizGenForm = {
    onClose: () => void,
    navigation: StepperNavigation<any>
}

export const QuizGenForm = ({onClose, navigation}: QuizGenForm) => {
    const form = useForm({
        defaultValues: {
            maxQuestions: null,
            sortBy: null,
            lang: null,
            timer: null,
            quizzes: []
        }
    });

    const { config } = useGameStore()
    const languages = Object.keys(config)
    const lang = useWatch({ control: form.control, name: "lang" })
    const {filterDomains, setQuestions} = useQuizGenForm(form)

    const close = () => {
        navigation.reset()
        form.reset()
        onClose()
    }

    const handleContinue = () => {
        setQuestions()
        navigation.next()
    }

    return (<FormProvider {...form}>
        <Field>
            <OptionSelector fieldName="lang" options={languages} placeholder="Select language" />
        </Field>
        {lang &&
            <Field>
                <SearchAndSelect
                    placeholder="Search domains"
                    fieldName="quizzes"
                    options={config[lang]} filterBy={filterDomains} />
            </Field>
        }
        <Disclosure>
            <DisclosureButton className="py-2 flex items-center gap-2">
                <Icon icon={"material-icon-theme:settings"} />Advance settings
            </DisclosureButton>
            <DisclosurePanel className="flex flex-col gap-3">
                <Field className={"flex justify-center items-center gap-1"}>
                    <Icon icon={'material-symbols:timer-outline-rounded'} />
                    <OptionSelector
                        fieldName="timer"
                        options={timerOptions}
                        placeholder="Select timer" />
                </Field>
                <Field className={"flex flex-col gap-1"}>
                    <OptionSelector
                        fieldName="maxQuestions"
                        options={maxQaOptions}
                        placeholder="Select max question per domain" />
                </Field>
                <Field className={"flex flex-col gap-1"}>
                    <OptionSelector
                        fieldName="sortBy"
                        options={sortOptionLabels}
                        placeholder="Select sort option" />
                </Field>
            </DisclosurePanel>
        </Disclosure>

        <div className="flex justify-evenly">
            <Button type={'Info'} label={"Continue"} onClick={handleContinue} />
            <Button type={'Neutro'} label={"Cancel"} onClick={close} />
        </div>
    </FormProvider>)
}
