export const sortOptions = ["domainName", "role", "level"] as const;
export type sortOptionType = typeof sortOptions[number];
const sortOptionsMap  = {

} as const;

export interface Question {
    question: string,
    answer: string,
    options: string[],
    tags: string[],
    level: number
}

export interface GameQuestion extends Question {
    domainName: string,
    role: string,
    level: number
}

export interface FileConfig {
    file: string,
    name: string
}