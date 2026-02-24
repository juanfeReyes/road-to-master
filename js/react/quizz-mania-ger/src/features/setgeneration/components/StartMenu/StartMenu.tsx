'use client'

import { useEffect } from "react"
import { QuizGenDialog } from "../QuizGenDialog/QuizGenDialog"
import { useFetcher } from "../../hooks/useFetcher"

export const StartMenu = () => {

    const {setConfigManifest} = useFetcher()
    
    useEffect(() => {
        setConfigManifest()
    }, [])

    return (<div className="flex flex-col items-center gap-3 h-dvh justify-center">
        <div className="py-5 flex flex-col text-7xl font-bold text-center bg-cyan-200 w-xl rounded-t-2xl">
            <span>Quizz</span>
            <span>Mania<span className="text-5xl">-gen</span></span>
        </div>
        <div className="flex flex-col gap-3 bg-amber-400 w-xl rounded-b-2xl text-2xl items-center" >
            <QuizGenDialog />
            <button className="pb-4 bg-amber-500 rounded-b-2xl w-full">Quit</button>
        </div>
    </div>)
}

