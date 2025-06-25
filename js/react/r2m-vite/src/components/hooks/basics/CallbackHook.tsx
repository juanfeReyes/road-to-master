import { memo, useCallback, useRef, useState, type ChangeEvent } from "react"
import { Card } from "../../shared/card/Card"
import { ProfilerBox } from "../../shared/ProfillerBox/ProfilerBox"

export const CallbackHook = () => {

  return (<Card title="Callback hook">
    <ProfilerBox
      id="heavy-computation"
      children={(counter) => <HeavyComputation />}
    />
    <ProfilerBox
      id="heavy-computation"
      children={(counter) => <HeavyComputationWithCallback />}
    />
  </Card>)
}

interface HeavyChildProps {
  extraCalculation: () => void
}
const HeavyChild = ({extraCalculation}: HeavyChildProps) => {

  const [maxComputations, setMaxComputations] = useState<string>('')

   const heavyCompute = () => {
    let i = 0;
    const max = maxComputations === '' ? 0 : Number(maxComputations)
    for (let j = 0; j < max; j++) {
      for (let x = 0; x < 10000; x++) {
        for (let y = 0; y < 10000; y++) {
          i = 0;
        }
      }
    }
    extraCalculation()
    return i % 2 === 0;
  }

  const showTitle = heavyCompute()
  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
      const max = event.target.value === '' ? '' : Math.min(Number(event.target.value), 10).toString()
      setMaxComputations(max)
    }

  return (<div>
    Heavy computation
    <input onChange={handleChange} value={maxComputations}></input>
  </div>)
}

const MemoHeavyChild = memo(HeavyChild)
const HeavyComputation = () => {

  const findItems = () => {
    return ['fake1', 'fake2']
  }
  
  return (<div>
    <MemoHeavyChild extraCalculation={findItems} />
  </div>)
}

const HeavyComputationWithCallback = () => {

  const findItems = useCallback(() => {
    return ['fake1', 'fake2']
  }, [])
  
  return (<div>
    <MemoHeavyChild extraCalculation={findItems} />
  </div>)
}
