import { createContext, useContext, useState } from "react"
import { Card } from "../../shared/card/Card"
import { CiCircleMinus, CiCirclePlus } from "react-icons/ci"

const CounterContext = createContext(0)

export const ContextHook = () => {
  const [counter, setCounter] = useState(0)

  const handleIncrease = () => {
      setCounter(counter => counter + 1)
    }
  
    const handleDecrease = () => {
      setCounter(counter => counter - 1)
    }

  return (<Card title="Context hook">
    <CounterContext value={counter}>
      <ContextChild />
      <button onClick={handleIncrease}><CiCirclePlus /></button>
      <button onClick={handleDecrease}><CiCircleMinus /></button>
    </CounterContext>
  </Card>)
}

const ContextChild = () => {

  return (<div>
    <ContextGrandChild />
  </div>)
}

const ContextGrandChild = () => {

  const counter = useContext(CounterContext)

  return (<div>
    {counter}
  </div>)
}
