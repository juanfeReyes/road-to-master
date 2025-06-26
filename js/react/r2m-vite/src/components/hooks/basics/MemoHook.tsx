'use no memo'

import { memo, useEffect, useMemo, useRef, useState, type ChangeEvent } from "react"
import { Card } from "../../shared/card/Card"
import { ProfilerBox } from "../../shared/ProfillerBox/ProfilerBox"

export const MemoHook = () => {

  return (<Card title="Memo hook">
    <ProfilerBox
      id="heavy-computation"
      children={(counter) => <HeavyComputation />}
    />
    <ProfilerBox
      id="heavy-computation"
      children={(counter) => <MemoizedComponent />}
    />
    <ProfilerBox
      id="heavy-computation"
      children={(counter) => <MemoComponent />}
    />
  </Card>)
}

/**
 * Baseline slow component
 * 
 * @returns 
 */
const HeavyComputation = () => {

  const maxComputations = useRef<string>('')

  /**
   * Perform a long and useless operation to 'simulate' slow react interaction-render time
   * @returns
   */
  const heavyCompute = () => {
    let i = 0;
    const max = maxComputations.current === '' ? 0 : Number(maxComputations.current)
    for (let j = 0; j < max; j++) {
      for (let x = 0; x < 10000; x++) {
        for (let y = 0; y < 10000; y++) {
          i = 0;
        }
      }
    }
    return i % 2 === 0;
  }

  const showTitle = heavyCompute()
  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    const max = event.target.value === '' ? '' : Math.min(Number(event.target.value), 10).toString()
    maxComputations.current = max
  }

  return (<div className="vertical">
    <p>{showTitle && 'Heavy compute'}</p>
    <input onChange={handleChange} value={maxComputations.current}></input>
  </div>
  )
}

const MemoizedComponent = () => {
  const maxComputations = useRef<string>('')

  /**
   * Perform a long and useless operation to 'simulate' slow react interaction-render time
   * @returns
   */
  const heavyCompute = () => {
    let i = 0;
    const max = maxComputations.current === '' ? 0 : Number(maxComputations.current)
    for (let j = 0; j < max; j++) {
      for (let x = 0; x < 10000; x++) {
        for (let y = 0; y < 10000; y++) {
          i = 0;
        }
      }
    }
    return i % 2 === 0;
  }

  const showTitle = useMemo(() => heavyCompute(), [maxComputations.current])
  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    const max = event.target.value === '' ? '' : Math.min(Number(event.target.value), 10).toString()
    maxComputations.current = max
  }


  return (<div className="vertical">
    <p>{showTitle && 'Memoized compute'}</p>
    <input onChange={handleChange} value={maxComputations.current}></input>
  </div>
  )
}


interface ConstantlyUpdateProps {
  maxComputations: string,
 }
const ConstantlyUpdate = ({maxComputations }: ConstantlyUpdateProps) => {

  return (<div className="box_border">
    Constantly updated component
  </div>)
}

interface HeavyComputeProps {
  maxComputations: string
 }
const HeavyCompute = ({maxComputations }: HeavyComputeProps) => {

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
    return i % 2 === 0;
  }

  const showTitle = heavyCompute()

  return (<div className="box_border">
    Child Memo heavy compute
  </div>)
}

const MemoHeavyCompute = memo(HeavyCompute);
const MemoComponent = () => {
  const maxComputations = useRef<string>('')

  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    const max = event.target.value === '' ? '' : Math.min(Number(event.target.value), 10).toString()
    maxComputations.current = max
  }

  return (<div>
    <input onChange={handleChange} value={maxComputations.current}></input>
    <ConstantlyUpdate maxComputations={maxComputations.current}/>
    <MemoHeavyCompute maxComputations={maxComputations.current}/>
  </div>)
}
