import { Question } from "./Question";


export interface Domain {
    level: number,
    name: string,
    role: string,
    tags: string[],
    quiz: Question[]
}
