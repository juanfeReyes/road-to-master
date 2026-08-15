'use client'

import { Icon } from "@iconify/react"
import somethingWentWrong from '@/public/something-went-wrong.png'
import Image from 'next/image';


export const ErrorFallback = ({ error, resetErrorBoundary, businessMessage }) => {
    console.log(error)

    return (
        <div className="flex flex-col items-center mt-5 gap-3">
            <h2 className="flex gap-3 text-4xl">Ups! Something went wrong!</h2>
            <Image
                src={somethingWentWrong}
                alt="Something went wrong"
            />
            <pre>{businessMessage}</pre>
            <button className="flex gap-2 justify-center items-center" onClick={resetErrorBoundary}>
                <Icon icon={'pajamas:retry'} /><span>Reload</span>
            </button>
        </div>
    )
}