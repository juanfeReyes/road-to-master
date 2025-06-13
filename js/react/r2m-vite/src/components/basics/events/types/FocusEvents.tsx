import type { FocusEvent } from "react"
import { Card } from "../Events"
import { NotificationContainer, type NotificationItemProps } from "../NotificationContainer"

export const FocusEvents = () => {

  return (<Card title={"Focus events"}>
    <NotificationContainer
      children={(handleNotification) => <Focus showNotification={handleNotification} />}
    />
  </Card>)
}

interface FocusProps extends NotificationItemProps { }
const Focus = ({ showNotification }: FocusProps) => {
  const handleFocus = (event: FocusEvent) => {
    showNotification('Focus event')
  }

  const handleBlur = (event: FocusEvent) => {
    showNotification('Blur event')
  }

  return (<div>
      <input onFocus={handleFocus} onBlur={handleBlur} data-testid="focus_input"/>
  </div>)
}
