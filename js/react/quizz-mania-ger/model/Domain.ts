import { Question } from "./Question";


interface Domain {
    level: string,
    name: string,
    tags: string[],
    quizz: Question[]
}
