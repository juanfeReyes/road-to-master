import { Dialog, DialogPanel, DialogTitle, Description } from "@headlessui/react"
import { useState } from "react"
import { QuizGenForm } from "../QuizGenForm/QuizGenForm"
import { Icon } from "@iconify/react"
import { defineStepper } from "@stepperize/react"
import { QuizGenResume } from "../QuizGenResume/QuizGenResume"

type QuizGenDialogProps = {
    open?: boolean,
    initialStep?: 'setup' | 'resume'
}

export const QuizGenDialog = ({ open, initialStep }: QuizGenDialogProps) => {
    let [isOpen, setIsOpen] = useState(open ?? false)
    const {useStepper} = defineStepper(
        {id: 'setup', title: 'Configure Quiz Generator'},
        {id: 'resume', title: 'Verify Quiz Generator'},
    )
    const {state, flow, navigation} = useStepper({initialStep: initialStep ?? 'setup'})

    return <>
        <button className="pt-4 flex items-center gap-1" onClick={() => setIsOpen(true)}><Icon icon={"fluent:quiz-20-filled"} />Quiz Generator</button>
        <Dialog open={isOpen} onClose={() => setIsOpen(false)} className="relative z-50">
            <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
                <DialogPanel className="p-3 min-w-xl min-h-xl bg-white space-y-4 border rounded-2xl shadow-2xl">
                    <DialogTitle className="font-bold flex items-center gap-1">
                        <Icon icon={"fluent:quiz-20-filled"} />{state.current.data.title}</DialogTitle>
                        {flow.switch({
                            setup: () => <QuizGenForm onClose={() => setIsOpen(false)} navigation={navigation}/>,
                            resume: () => <QuizGenResume onClose={() => setIsOpen(false)} navigation={navigation}/>
                        })}
                </DialogPanel>
            </div>
        </Dialog>
    </>
}

