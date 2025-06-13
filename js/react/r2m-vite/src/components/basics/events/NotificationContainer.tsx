import { useState, type JSX } from "react"

export interface NotificationContainerProps {
  children: (showNotification: (msg: string) => void) => JSX.Element,
  timeout?: number
}
export interface NotificationItemProps {
  showNotification: (msg: string) => void
}

export const NotificationContainer = ({ children, timeout }: NotificationContainerProps) => {
  const [show, setShow] = useState(false)
  const [message, setMessage] = useState('')

  /**
   * TODO: Improve notification to show history of notification
   * becareful avoid infinite event handling
   * @param msg 
   */
  const showNotification = (msg: string) => {
    console.log(msg)
    setMessage(msg)
    setShow(true)
    closeNotification(timeout || 5)
  }

  const closeNotification = (seconds: number) => {
    setTimeout(() => {
      setShow(false)
      setMessage('')
    }, seconds * 1000)
  }

  return (<div className="notification_container">
    {children(showNotification)}
    {show && <div data-testid="notification_msg" className="notification">{message}</div>}
  </div>)
}

