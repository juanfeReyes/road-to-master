import { type ChangeEvent, type FormEvent, type InputEvent, type InvalidEvent, type SyntheticEvent } from "react"
import { Card } from "../Events"
import { NotificationContainer, type NotificationItemProps } from "../NotificationContainer"

export const FormEvents = () => {

  return (<Card title={"Form events"}>
    <NotificationContainer
      children={(handleNotification) => <Form showNotification={handleNotification} />}
    />
  </Card>)
}

interface FormProps extends NotificationItemProps { }
const Form = ({ showNotification }: FormProps) => {

  /**
   * Launch at blur time once all value updates have been performed
   * @param event 
   */
  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    showNotification(`Change event ${event.target.value}`)
  }

  /**
   * Launch at submit time
   * @param event 
   */
  const handleInvalid = (event: InvalidEvent<HTMLInputElement>) => {
    showNotification(`invalid event ${event.target.value}`)
  }

  /**
   * Real time launch, which is quite heavy on performance. Usefull for field monitoring
   * @param event 
   */
  const handleInput = (event: InputEvent<HTMLInputElement>) => {
    showNotification(`input event`)
  }

  const handleSubmit = (event: SyntheticEvent<HTMLFormElement>) => {
    event.preventDefault()
    showNotification(`Submit event`)
  }

  const handleReset = (event: SyntheticEvent<HTMLFormElement>) => {
    event.preventDefault()
    showNotification(`Reset event`)
  }

  return (<div>
    <form onSubmit={handleSubmit}
      onReset={handleReset}>
      <input type="number" max={5}
        data-testid="form_input"
        onChange={handleChange}
        onInvalid={handleInvalid}
        onInput={handleInput} />
      <div>
        <button type="submit" data-testid="form_submit_btn">submit</button>
        <button type="reset" data-testid="form_reset_btn">reset</button>
      </div>
    </form>
  </div>)
}
