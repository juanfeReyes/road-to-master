import { useState } from "react"
import { Card } from "../../shared/card/Card"

export const StateHook = () => {

  const [counter, setCounter] = useState(0)

  const increaseCounter = () => {
    setCounter(counter + 1)
  }

  /**
   * Increases correctly as it has the up-to-date counter given when the execution is ran.
   */
  const asyncIncreaseCounter = () => {
    setTimeout(() => {
      setCounter(state => state + 1)
    }, 1000);
  }

  /**
   * Does not increase correctly as the execution is delayed and counter (data) is stale
   */
  const asyncIncreaseCounterNoFunction = () => {
    setTimeout(() => {
      setCounter(counter + 1)
    }, 1000);
  }

  return (<Card title={"State hook"}>
    <div className="horizontal">
      {counter}
      <button onClick={increaseCounter}>increase</button>
      <button onClick={asyncIncreaseCounter}>async increase</button>
      <button onClick={asyncIncreaseCounterNoFunction}>async increase no function</button>
    </div>
  </Card>)
}
