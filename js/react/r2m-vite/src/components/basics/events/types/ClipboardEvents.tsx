import type { ClipboardEvent } from "react"
import { Card } from "../Events"
import { NotificationContainer, type NotificationItemProps } from "../NotificationContainer"

export const ClipboardEvents = () => {


  return (<Card title={"Clipboard events"}>
    <NotificationContainer
      children={(handleNotification) => <Clipboard showNotification={handleNotification} />}
    />
  </Card>)
}

interface ClipboardProps extends NotificationItemProps { }
const Clipboard = ({ showNotification }: ClipboardProps) => {
  const handleCopy = (event: ClipboardEvent) => {
    showNotification("Copy event")
  }

  const handleCut = (event: ClipboardEvent) => {
    showNotification("Cut event")
  }

  const handlePaste = (event: ClipboardEvent) => {
    showNotification("Paste event")
  }

  return (<div>
    <div>
      <p>Copy or cut</p>
      <input onCopy={handleCopy} onCut={handleCut} data-testid="clipboard_copy_input" />
    </div>
    <div>
      <p>Paste here</p>
      <input onPaste={handlePaste} data-testid="clipboard_paste_input"/>
    </div>
  </div>)
}
