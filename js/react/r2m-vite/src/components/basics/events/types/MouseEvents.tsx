import { useState, type DragEvent, type MouseEvent } from "react"
import { NotificationContainer, type NotificationItemProps } from "../NotificationContainer"

import '../Events.css'
import { Card } from "../Events"

export const MouseEvents = () => {

  return (<Card title="Mouse events">
    <NotificationContainer
      children={(handleNotification) => <Button showNotification={handleNotification} />}
    />
    <NotificationContainer
      children={(handleNotification) => <DragZone showNotification={handleNotification} />}
    />
    <NotificationContainer
      timeout={0}
      children={(handleNotification) => <MouseZone showNotification={handleNotification} />}
    />
  </Card>)
}

interface ButtonProps extends NotificationItemProps { }
const Button = ({ showNotification }: ButtonProps) => {
  const handleClick = () => {
    showNotification('Click notification')
  }

  const handleDoubleClick = () => {
    showNotification('Double click notification')
  }

  return (
    <button data-testid="mouse_click_btn" onClick={handleClick} onDoubleClick={handleDoubleClick}>Button</button>
  )
}

interface DragZoneProps extends NotificationItemProps { }
const DragZone = ({ showNotification }: DragZoneProps) => {
  const [currentSlot, setCurrentSlot] = useState('A')

  const handleDrag = (event: DragEvent<HTMLParagraphElement>) => {
    showNotification(`Drag event ${event.screenX} - ${event.screenY}`)
  }

  /**
   * Highligth the element and make it look like it is floating
   * 
   * @param event 
   */
  const handleDragStart = (event: DragEvent<HTMLParagraphElement>) => {
    showNotification(`Drag start event`)
  }

  /**
   * Append to dragZone
   * @param event 
   */
  const handleDragEnd = (event: DragEvent<HTMLParagraphElement>) => {
    showNotification(`Drag end event`)
  }

  /**
   * Thicken dragzone border
   * @param event 
   */
  const handleDragEnter = (event: DragEvent<HTMLParagraphElement>) => {
    showNotification(`Drag enter event`)
  }

  /**
   * Normalize dragzone border
   * @param event 
   */
  const handleDragLeave = (event: DragEvent<HTMLParagraphElement>) => {
    showNotification(`Drag leave event`)
  }

  /**
   * Change color when entering the zone
   * @param event 
   */
  const handleDragOver = (event: DragEvent<HTMLParagraphElement>) => {
    showNotification(`Drag over event`)
    event.preventDefault()
  }

  const handleDrop = (event: DragEvent<HTMLParagraphElement>) => {
    showNotification(`drop event`)
    setCurrentSlot(event.currentTarget.id)
  }


  return (
    <div>
      <div id="A" className="drag-zone_zontainer"
        onDragStart={handleDragStart}
        onDragEnd={handleDragEnd}
        onDragEnter={handleDragEnter}
        onDragLeave={handleDragLeave}
        onDragOver={handleDragOver}
        onDrop={handleDrop}
        data-testid="mouse_drag_zoneA"
      >
        {currentSlot == 'A' &&
          <p data-testId="mouse_drag_textA" draggable="true" onDrag={handleDrag}>Drag me!</p>
        }
      </div>
      <div id="B" className="drag-zone_zontainer"
        onDragStart={handleDragStart}
        onDragEnd={handleDragEnd}
        onDragEnter={handleDragEnter}
        onDragLeave={handleDragLeave}
        onDragOver={handleDragOver}
        onDrop={handleDrop}
        data-testid="mouse_drag_zoneB"
      >
        {currentSlot == 'B' &&
          <p data-testId="mouse_drag_textB" draggable="true" onDrag={handleDrag}>Drag me!</p>
        }
      </div>
    </div>
  )
}

interface MouseZoneProps extends NotificationItemProps { }
const MouseZone = ({ showNotification }: MouseZoneProps) => {

  /**
   * Mouse down is triggered when user clicks on listener element
   * 
   * Click is launch when button pressed and released
   */
  const handleMouseDown = () => { showNotification('Mouse down') }
  const handleMouseUp = () => { showNotification('Mouse up') }

  const handleMouseEnter = () => { showNotification('Mouse enter') }
  const handleMouseLeave = () => { showNotification('Mouse leave') }
  const handleMouseMove = (event: MouseEvent) => {
    event.preventDefault()
    showNotification('Mouse move')
  }
  const handleMouseOut = () => { showNotification('Mouse out') }
  const handleMouseOver = () => { showNotification('Mouse over') }


  return (<div data-testid="mouse_event_zone" className="drag-zone_zontainer"
    onMouseDown={handleMouseDown}
    onMouseEnter={handleMouseEnter}
    onMouseOut={handleMouseOut}
    onMouseLeave={handleMouseLeave}
    onMouseMove={handleMouseMove}
    onMouseOver={handleMouseOver}
    onMouseUp={handleMouseUp}
  >
    <button data-testid="mouse_btn">Click me!</button>
  </div>)

}
