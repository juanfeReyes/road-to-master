'use client'
import React, { PropsWithChildren, useState } from "react";

interface QuizzCardProps {
    question: string,
    answer: string,
}

const QuizzCard = ({ question, answer }: PropsWithChildren<QuizzCardProps>) => {
    const [showQuestion, setShowQuestion] = useState(true)

    const switchShow = () => setShowQuestion(show => !show)
    const containerStyle = showQuestion ? "bg-blue-700 text-gray-100" : 'bg-blue-300'
    return (<div className={`rounded-2xl p-3 content-center flex justify-center text-2xl ${containerStyle}`} onClick={switchShow}>
        {showQuestion ?
            <>{question}</>
            :
            <>{answer}</>
        }
    </div>)
}

export default QuizzCard;
