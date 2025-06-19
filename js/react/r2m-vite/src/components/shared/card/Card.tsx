import type { PropsWithChildren } from "react"

import './Card.css'

export interface CardProps extends PropsWithChildren {
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