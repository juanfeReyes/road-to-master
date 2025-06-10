import { useEffect, useRef, useState, type Ref, type RefObject } from "react"
import './Basics.css'

export const Basics = () => {

  const ref = useRef<HTMLButtonElement>(null);

  // Initial state setup
  const initialCount = +(localStorage.getItem('count') || 0);
  const [count, setCount] = useState(initialCount)
  const handleIncrease = () => setCount(count + 1)
  const handleDecrease = () => setCount(count - 1)

  useEffect(() => {
    // Execute when component just mounted or updated
    localStorage.setItem('count', count.toString())
    ref.current?.focus();
    return () => {
      // Execute when component will unmount
    }
  },
  // limit effect only for count update
    [count])

  return (<>
    <div className="count_container">
      <p data-testid="counter" className="counter_container">count: {count}</p>
      <div className="count_btn_container">
        <Button ref={ref} onClickHandler={handleIncrease} text="Increase" testId="increase-btn"/>
        <Button onClickHandler={handleDecrease} text="Decrease" testId="decrease-btn"/>
      </div>
    </div>
  </>)
}

// Props sent to the Button component
interface ButtonProps {
  ref?: RefObject<HTMLButtonElement | null>
  onClickHandler: () => void
  text: string
  testId: string
}

const Button = ({ref, onClickHandler, text, testId}: ButtonProps) => {

  return <button ref={ref || null} data-testid={testId} onClick={onClickHandler}>{text}</button>
}
