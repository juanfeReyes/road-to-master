
import type { ComponentType, JSX, ReactElement } from 'react';
import './Conditional.css'

export const conditionOptions = {
  OK: 'Ok',
  WARNING: 'Warning',
  ERROR: 'Error'
} as const

type ObjectValues<T> = T[keyof T];
export type ConditionOptions = ObjectValues<typeof conditionOptions>

interface ConditionalProps {
  showCondition?: boolean,
  option?: ConditionOptions
}

export const Conditional = ({ showCondition, option }: ConditionalProps) => {

  return (<div className='container'>
    <IfRender showCondition={showCondition} />
    <TernaryRender showCondition={showCondition} />
    <SwitchRender option={option} />
    <HighOrderConditional option={option} />
  </div>)
}

const IfRender = ({ showCondition }: ConditionalProps) => {

  if (showCondition) {
    return (<div className='item'>If conditional</div>)
  }

  return <div className='item'>Happy render</div>
}

const TernaryRender = ({ showCondition }: ConditionalProps) => {

  return <>
    { showCondition ? 
      <div className='item'>Ternary conditional</div> :
      <div className='item'>Happy render</div>
    }
  </>
}

const SwitchRender = ({ option }: ConditionalProps) => {

  switch (option) {
    case conditionOptions.OK:
      return <div className='item'>OK conditional</div>;
    case conditionOptions.WARNING:
      return <div className='item'>Warning conditional</div>;
    case conditionOptions.ERROR:
      return <div className='item'>Error conditional</div>;
    default:
      return <div className='item'>Happy render</div>
  }
}

interface MessageProps {
  msg: string | undefined
}
const LoadIfSelected = (Component: ComponentType<MessageProps>) => {
  return (props: MessageProps) => {
    if (!!props.msg) {
      return <Component {...props}/>
    }

    return <div className='item'>Happy render</div>
  }
}
const Message = ({msg}: MessageProps) => {
  return <div className='item'>{msg} conditional</div>
}

const MessageWithCondition = LoadIfSelected(Message)
const HighOrderConditional = ({ option }: ConditionalProps) => {
    return <MessageWithCondition msg={option?.toString()}/>
}



