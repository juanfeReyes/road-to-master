import { useReducer } from "react"
import { CiCircleMinus } from "react-icons/ci";
import { CiCirclePlus } from "react-icons/ci";
import { Card } from "../../shared/card/Card";

export const counterActionType = {
  INCREASE: 'INCREASE',
  DECREASE: 'DECREASE'
}
type ObjectValues<T> = T[keyof T];
export type CounterActionType = ObjectValues<typeof counterActionType>

interface CounterAction {
  type: CounterActionType,
  payload?: CounterState
}

interface CounterState {
  counter: number
}

const reducer = (state: CounterState, action: CounterAction) => {
  switch (action.type) {
    case counterActionType.INCREASE:
      return { ...state, counter: state.counter + 1 }
    case counterActionType.DECREASE:
      return { ...state, counter: state.counter - 1 }
    default:
      return state
  }
}

const initialState: CounterState = { counter: 0 }

export const ReducerHook = () => {

  const [state, dispatch] = useReducer(reducer, initialState)

  const handleIncrease = () => {
    dispatch({ type: counterActionType.INCREASE })
  }

  const handleDecrease = () => {
    dispatch({ type: counterActionType.DECREASE })
  }

  return (<Card title="Reducer hook">
    <div className="vertical">
      <p>Counter: {state.counter}</p>
      <div className="horizontal">
        <button onClick={handleIncrease}><CiCirclePlus /></button>
        <button onClick={handleDecrease}><CiCircleMinus /></button>
      </div>
    </div>
  </Card>)
}
