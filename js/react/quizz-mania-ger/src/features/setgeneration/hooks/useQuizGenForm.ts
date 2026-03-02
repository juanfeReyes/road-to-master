import router from "next/router"
import { SearchAndSelectOption } from "../../common/components/SearchAndSelect/SearchAndSelect"
import { useShuffler } from "./useShuffler"
import { UseFormReturn, useWatch } from "react-hook-form"
import { useRouter } from "next/navigation"
import { SetupActionType, useGameStore } from "../../common/store/GameStore"
import { maxQaOptions, sortOptionLabels, sortOptionLabelTypes, sortOptionsMap } from "@/src/features/common/model/Options"
import { useFetcher } from "./useFetcher"

export const useQuizGenForm = (form: UseFormReturn<any>) => {
    const router = useRouter()
    const { buildQuestionGroups, shuffle } = useShuffler()
    const quizzes = useWatch({ control: form.control, name: "quizzes" })
    const { initializeGame, currentGame } = useGameStore()
    const { fetchDomains } = useFetcher()

    const filterDomains = (option: SearchAndSelectOption, query: string) => {
        return (option.name.toLowerCase().includes(query.toLowerCase()) ||
            (option.tags as string[]).some(tag => tag.toLowerCase().includes(query.toLowerCase()))) &&
            !quizzes.map(q => q.name).includes(option.name)
    }

    const setQuestions = async () => {
        const sortLabel: sortOptionLabelTypes  = form.getValues("sortBy") ?? sortOptionLabels[0]
        const sortBy = sortOptionsMap[sortLabel]
        const maxQuestions = (form.getValues("maxQuestions") ?? maxQaOptions[0]) as number
        const domains = await fetchDomains(quizzes)
        const groups = buildQuestionGroups(domains, sortBy, maxQuestions)
        const questions = shuffle(groups, sortBy)
        const setup: SetupActionType = {
            sortBy: sortLabel,
            timer: form.getValues("timer"),
            questions: questions,
            resume: groups
        }
        initializeGame(setup)
    }

    return {filterDomains, setQuestions}
}
