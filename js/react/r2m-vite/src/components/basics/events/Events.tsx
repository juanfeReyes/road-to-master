import type { PropsWithChildren } from 'react'
import './Events.css'
import { MouseEvents } from "./types/MouseEvents"
import { ClipboardEvents } from './types/ClipboardEvents'
import { FocusEvents } from './types/FocusEvents'
import { FormEvents } from './types/FormEvents'
import { KeyBoardEvents } from './types/KeyboardEvents'

export const Events = () => {

  return (<div className="container">
    <MouseEvents />
    <ClipboardEvents />
    <FocusEvents />
    <FormEvents />
    <KeyBoardEvents />
  </div>)
}

interface CardProps extends PropsWithChildren {
  title: string,
}
export const Card = ({ children, title }: CardProps) => {

  return (<div className='card'>
    <h2>{title}</h2>
    <div className="container">
      {children}
    </div>
  </div>)
}



