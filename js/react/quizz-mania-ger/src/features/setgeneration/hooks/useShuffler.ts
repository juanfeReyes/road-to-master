import { Domain } from "@/src/features/common/model/Domain"
import { arrayToShuffled } from 'array-shuffle';
import { useFetcher } from "./useFetcher";
import { FileConfig, GameQuestion, Question, sortOptionType } from "@/src/features/common/model/Question";

export const useShuffler = () => {
    const { fetchDomains } = useFetcher()

    const buildQuestionGroups = (domains: Domain[], sortBy: sortOptionType, maxQuestions: number): Record<string | number, GameQuestion[]> => {
        // build GameQuestion
        const gameQuestions: GameQuestion[] = domains.flatMap(domain => domain.quiz.map(q => ({
            ...q,
            domainName: domain.name,
            role: domain.role,
            tags: domain.tags
        })))

        const groups = Object.groupBy(gameQuestions, (quest) => quest[sortBy])
        const shuffled = Object.entries(groups).map(([key, quest]) => ([key, arrayToShuffled(quest)]))
        const spliced = shuffled.map(([key, quest]) => ([key, quest?.toSpliced(maxQuestions)]))

        return Object.fromEntries(spliced);
    }

    const shuffle = (groups: Record<string | number, GameQuestion[]>, sortBy: sortOptionType) => {
        const flat = Object.values(groups).flat();
        const sorted = flat.sort((a, b) => a[sortBy] - b[sortBy])
        return sorted;
    }

    return { shuffle, buildQuestionGroups }
}
