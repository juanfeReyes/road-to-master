import { useEffect, useRef, useState, type RefObject } from "react"
import { Card } from "../../shared/card/Card"

export const EffectHook = () => {

  return (<Card title="Effect hook">
    <MountAndUpdate />
    <MountOnly />
    <VariableUpdatedOnly />
    <Unmount />
  </Card>)
}

const MountAndUpdate = () => {
  const [counter, setCounter] = useState(0)
  const effectCounter = useRef(0)

  useEffect(() => {
    effectCounter.current = effectCounter.current + 1
  })

  const increaseCounter = () => {
    setCounter(state => state + 1)
  }

  return (<div className="box_border">
    <h4>Mount and update</h4>
    <div>
      <p>Counter: {counter}</p>
      <p>Effect counter: {effectCounter.current}</p>
    </div>
    <button onClick={increaseCounter}>increase counter</button>
  </div>)
}

const MountOnly = () => {

  const [counter, setCounter] = useState(0)
  const effectCounter = useRef(0)

  useEffect(() => {
    effectCounter.current = effectCounter.current + 1
  }, [])

  const increaseCounter = () => {
    setCounter(state => state + 1)
  }

  return (<div className="box_border">
    <h4>Mount only</h4>
    <div>
      <p>Counter: {counter}</p>
      <p>Effect counter: {effectCounter.current}</p>
    </div>
    <button onClick={increaseCounter}>increase counter</button>
  </div>)
}

const VariableUpdatedOnly = () => {

  const [counter, setCounter] = useState(0)
  const effectCounter = useRef(0)
  const didMount = useRef(false)

  useEffect(() => {
    if (didMount.current) {
      effectCounter.current = effectCounter.current + 1
    } else {
      didMount.current = true
    }
  }, [counter])

  const increaseCounter = () => {
    setCounter(state => state + 1)
  }

  return (<div className="box_border">
    <h4>Update only</h4>
    <div>
      <p>Counter: {counter}</p>
      <p>Effect counter: {effectCounter.current}</p>
    </div>
    <button onClick={increaseCounter}>increase counter</button>
  </div>)
}

const Unmount = () => {
const [counter, setCounter] = useState(0)
  const effectCounter = useRef(0)

  const increaseCounter = () => {
    setCounter(state => state + 1)
  }

  return (<div className="box_border">
    <h4>Unmount</h4>
    <div>
      <p>Counter: {counter}</p>
      <p>Effect counter: {effectCounter.current}</p>
    </div>
    <button onClick={increaseCounter}>increase counter</button>
    {(counter % 2 === 0) && <UnmountChild effectCounter={effectCounter}/>}
  </div>)
}

interface UnmountChildProps {
  effectCounter: RefObject<number>
}

const UnmountChild = ({effectCounter}: UnmountChildProps) => {

  useEffect(() => {

    return () => {
      console.log('unmount')
      effectCounter.current = effectCounter.current + 1;
    }
  })

  return (<div>
    unmount me!
  </div>)
}
