import { DialogPanel, DialogTitle, Description, Dialog } from "@headlessui/react"
import { Dispatch, ReactNode, SetStateAction, useState } from "react"
import { Header } from "./Header"
import { Button } from "./Button"

export type CustomDialogProps = {
    label: string,
    icon: string,
    content: (setIsOpen:  Dispatch<SetStateAction<boolean>>) => ReactNode
}

export const CustomDialog = ({label, content, icon}: CustomDialogProps) => {
    let [isOpen, setIsOpen] = useState(false)

    return (
        <>
            <Button label={<Header icon={icon} label={label} />} onClick={() => setIsOpen(true)} type="Primary" />
            <Dialog open={isOpen} onClose={() => setIsOpen(false)} className="relative z-50">
                <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
                    <DialogPanel className="max-w-lg space-y-4 border bg-white p-12 rounded-2xl shadow-2xl">
                        {content(setIsOpen)}
                    </DialogPanel>
                </div>
            </Dialog>
        </>
    )
}
