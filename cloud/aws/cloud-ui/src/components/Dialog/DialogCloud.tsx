import { Button, Dialog, DialogPanel, DialogTitle } from "@headlessui/react"
import { PropsWithChildren, useState } from "react"

interface DialogCloudProps {
  title: string,
  onCancel: () => void,
  onSubmit: () => void
}

export const DialogCloud = ({title, onCancel, onSubmit, children}: PropsWithChildren<DialogCloudProps>) => {
  const [isOpen, setIsOpen] = useState(false)

  const handleOnClick = () => {
    setIsOpen(false)
  }

  const handleOnCancel = () => {
    onCancel()
    handleOnClick()
  }

  const handleOnSubmit = () => {
    onSubmit()
    handleOnClick()
  }

  return (
    <div>
      <Button
        onClick={() => setIsOpen(true)}
        className="rounded-md bg-black/20 py-2 px-4 text-sm font-medium text-white focus:outline-none data-[hover]:bg-black/30 data-[focus]:outline-1 data-[focus]:outline-white"
      >
        Open
      </Button>
      <Dialog open={isOpen} onClose={() => setIsOpen(false)} className="relative z-50">
        <div className="fixed inset-0 flex w-screen items-center justify-center p-4">
          <DialogPanel className="max-w-lg space-y-4 border bg-white p-12">
            <DialogTitle className="font-bold">{title}</DialogTitle>
            {children}
            <div className="flex justify-center gap-4">
              <button  onClick={handleOnCancel}>Cancel</button>
              <button onClick={handleOnSubmit}>Submit</button>
            </div>
          </DialogPanel>
        </div>
      </Dialog>
    </div>
  )
}
