
import { useState, type ComponentType, type JSX, type PropsWithChildren } from 'react';
import './Reusability.css';



/**
 * Composition requires to create a Parent component to handle generic logic
 * and state should be in the root component (lifting state).
 * 
 * Enhancing logic of child component requires a ParentComponent
 */
const Composition = () => {

  const [number, setNumber] = useState(0)

  const handleRandomize = () => {
    setNumber(Math.random())
  }

  return (
    <ParentComponent handleRandomize={handleRandomize}>
      <ChildComponent value={number} />
    </ParentComponent>
  )
}

interface ParentComponentProps extends PropsWithChildren {
  handleRandomize: () => void
}
const ParentComponent = ({ children, handleRandomize }: ParentComponentProps) => {

  return (
    <div className='item'>
      <h2>Composition Randomizer</h2>
      {children}
      <div>
        <button onClick={handleRandomize}>Randomize</button>
      </div>
    </div>
  )
}

interface ChildComponentProps {
  value: number
}
const ChildComponent = ({ value }: ChildComponentProps) => {
  return (<div>
    <p>{value}</p>
  </div>)
}

interface HocEnhancingProps {
  multiplier: number
}
/**
 * Enhanced the child component with having to lift state.
 * 
 * @param Component 
 * @returns 
 */
const withHighOrderComponent = (Component: ComponentType<ChildComponentProps>) => {

  return ({ multiplier }: HocEnhancingProps) => {

    const [number, setNumber] = useState(0)

    const handleRandomize = () => {
      setNumber(Math.random())
    }

    return (<div className='item'>
      <h2>HoC Randomizer</h2>
      <Component value={number * multiplier} />
      <div>
        <button onClick={handleRandomize}>Randomize</button>
      </div>
    </div>)
  }
}

interface RenderPropsComponentProps {
  children: (value: number) => JSX.Element,
  multiplier: number
}
/**
 * Enhanced version of composition allowing to pass props to child component without lifting the state
 * 
 * @param param0 
 * @returns 
 */
const RenderPropsComponent = ({ children, multiplier }: RenderPropsComponentProps) => {
  const [number, setNumber] = useState(0)

  const handleRandomize = () => {
    setNumber(Math.random())
  }


  return (<div className='item'>
    <h2>Render-Props Randomizer</h2>
    {children(number*multiplier)}
    <div>
      <button onClick={handleRandomize}>Randomize</button>
    </div>
  </div>)
}

const WithHocEnhanced = withHighOrderComponent(ChildComponent)
export const Reusability = () => {

  return (<div className='container'>
    <Composition />
    <WithHocEnhanced multiplier={100} />
    <RenderPropsComponent multiplier={1000} children={(value) => <ChildComponent value={value}/>} />
  </div>)
}