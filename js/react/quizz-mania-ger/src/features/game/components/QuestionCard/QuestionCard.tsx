'use client'
import { Question } from "@/src/features/common/model/Question";
import React, { useState } from "react";

interface MemoryCardProps {
    question: Question,
    onCorrectAnswer: () => void
    onWrongAnswer: () => void
}

const MemoryCard = ({ question, onCorrectAnswer, onWrongAnswer }: MemoryCardProps) => {
    const [showQuestion, setShowQuestion] = useState(true)
    const containerStyle = showQuestion ? "bg-blue-700 text-gray-100" : 'bg-blue-300'

    const switchShow = () => setShowQuestion(show => !show)
    const onContinue = (callback: () => void) => {
        switchShow()
        callback()
    }
    
    return (<div className={`rounded-2xl p-3 content-center flex justify-center text-2xl ${containerStyle}`}>
        {showQuestion ?
            <div onClick={switchShow}>{question.question}</div>
            :
            <div className="flex flex-col gap-5">
                <p>{question.answer}</p>
                <div className="flex justify-evenly">
                    <button onClick={() => onContinue(onCorrectAnswer)}>OK</button>
                    <button onClick={() => onContinue(onWrongAnswer)}>BAD</button>
                </div>
            </div>
        }
    </div>)
}

export default MemoryCard;
