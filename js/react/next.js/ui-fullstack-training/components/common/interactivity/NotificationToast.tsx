'use client'

import { createContext, Dispatch, PropsWithChildren, SetStateAction, useEffect, useState } from "react"
import * as motion from "motion/react-client"
import { AnimatePresence } from "motion/react"


type NotificationToastProps = {
    initTimeout?: number
}

type NotificationMessage = {
    value: string,
    type: 'INFO' | 'ERROR' | 'WARNING'
}
type NotificationContextType = {
    message?: NotificationMessage,
    notify: (msg: NotificationMessage) => void
}

export const NotificationContext = createContext<NotificationContextType>({
    notify: (msg: NotificationMessage) => { }
})


type ToastProps = {
    msg: NotificationMessage
}
const Toast = ({ msg }: ToastProps) => {
    const typeStyles = {
        'INFO': 'border-green-700',
        'ERROR': 'border-red-700',
        'WARNING': 'border-amber-600'
    } as const
    return (<div className="absolute top-0 right-0 ">
        <motion.div
            className={`absolute top-0 right-0 bg-white w-xl p-2 my-3 mr-1 rounded-lg border-b-4 ${typeStyles[msg?.type]}`}
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5 }}
            exit={{ opacity: 0, y: -20 }}
        >
            {msg?.value}
        </motion.div>
    </div>)
}

export const NotificationToast = ({ children, initTimeout }: PropsWithChildren<NotificationToastProps>) => {
    const timeout = initTimeout ?? 3000;
    const [show, setShow] = useState(false)
    const [message, setMessage] = useState<NotificationMessage>({ value: "TESSSSST", type: 'INFO' })

    useEffect(() => {
        if (!show) return;

        const timer = setTimeout(() => {
            setShow(false)
        }, timeout)

        return () => clearTimeout(timer)
    }, [show])

    const notify = (msg: NotificationMessage) => {
        setMessage(msg)
        setShow(true)
    }

    return (<NotificationContext.Provider value={{ message, notify }}>
        <div className={"relative"}>
            <AnimatePresence>
                {show && <Toast msg={message} />}
            </AnimatePresence>
            {children}
        </div>
    </NotificationContext.Provider>)
}
