'use client'
import React, { useState } from "react";
import MemoryCard from "../QuizzCard/modes/MemoryCard/MemoryCard";
import { Question } from "@/model/Question";
import { GameReport } from "./GameReport";

interface GameProps {
    questions: Question[]
}

export const Game = ({ questions }: GameProps) => {
    const [isGameOver, setIsGameOver] = useState(false)
    const [score, setScore] = useState(0)
    const [currentQuestion, setCurrentQuestions] = useState(0)
    const [correctAnswers, setCorrectAnswers] = useState<Question[]>([])
    const [wrongAnswers, setWrongAnswers] = useState<Question[]>([])

    const increaseScore = () => setScore(score => score + 1)
    const nextQuestion = () => {
        if (currentQuestion === questions.length-1) {
            setIsGameOver(gameOver => !gameOver)
            return;
        }
        console.log(currentQuestion)
        setCurrentQuestions(curr => curr + 1)
    }
    const onCorrectAnswer = () => {
        setCorrectAnswers(ans => [...ans, questions[currentQuestion]])
        increaseScore()
        nextQuestion()
    }
    const onWrongAnswer = () => {
        setWrongAnswers(ans => [...ans, questions[currentQuestion]])
        nextQuestion()
    }

    return (<>
        {isGameOver ?
            <GameReport correctAnswers={correctAnswers} wrongAnswers={wrongAnswers} />:
            <MemoryCard
                question={questions[currentQuestion]}
                onCorrectAnswer={onCorrectAnswer}
                onWrongAnswer={onWrongAnswer} />
        }

    </>)
}
