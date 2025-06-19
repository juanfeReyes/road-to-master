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




