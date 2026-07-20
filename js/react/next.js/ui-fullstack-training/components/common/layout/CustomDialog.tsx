import { DialogPanel, Dialog } from "@headlessui/react"
import { Dispatch, ReactNode, SetStateAction, useState } from "react"

export type CustomDialogProps = {
    button: (setIsOpen: Dispatch<SetStateAction<boolean>>) => ReactNode,
    content: (setIsOpen: Dispatch<SetStateAction<boolean>>) => ReactNode
}

export const CustomDialog = ({ content, button }: CustomDialogProps) => {
    let [isOpen, setIsOpen] = useState(false)

    return (
        <>
            {button(setIsOpen)}
            <Dialog open={isOpen} onClose={() => setIsOpen(false)} className="relative z-50">
                <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
                    <DialogPanel className="space-y-4 border bg-white p-12 rounded-2xl shadow-2xl">
                        {content(setIsOpen)}
                    </DialogPanel>
                </div>
            </Dialog>
        </>
    )
}
