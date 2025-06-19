import type { KeyboardEvent } from "react"
import { NotificationContainer, type NotificationItemProps } from "../NotificationContainer"
import { Card } from "../../../shared/card/Card"

export const KeyBoardEvents = () => {

  return (<Card title={"Keyboard events"}>
    <NotificationContainer
      children={(handleNotification) => <Keyboard showNotification={handleNotification} />}
    />
  </Card>)
}

interface KeyboardProps extends NotificationItemProps { }
const Keyboard = ({ showNotification }: KeyboardProps) => {
  const handleKeyDown = (event: KeyboardEvent) => {
    showNotification(`Key down event ${event.key}`)
  }

  const handleKeyUp = (event: KeyboardEvent) => {
    showNotification(`Key up event ${event.key}`)
  }

  return (<div
    className="drag-zone_zontainer">
    <input data-testid="keyboard_event_input"
      onKeyDown={handleKeyDown}
      onKeyUp={handleKeyUp} />
  </div>)
}
