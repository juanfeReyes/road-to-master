import { useEffect, useRef, useState } from "react"

export const Refs = () => {
  const counter = useRef(0);
  const elemRef = useRef<HTMLDivElement>(null)
  const [containerSize, setContainerSize] = useState({})

  // Called after component is mounter and elemRef is already set
  useEffect(() => {
    handleReRender()
    counter.current++
  }, [])

  // Updating references do not launch render component
  // Good for updating data with low performance impact
  const handleIncrease = () => {
    counter.current++
  }

  const handleReRender = () => {
    const boudries = elemRef.current?.getBoundingClientRect();
    setContainerSize({
      width: boudries?.right - boudries?.left,
      heigth: boudries?.bottom - boudries?.top
    })
  }

  return (<div ref={elemRef}>
    <h2>Ref:</h2>
    <p>Container size: {containerSize.width} - {containerSize.heigth}</p>
    <p>Counter: {counter.current}</p>
    <button onClick={handleIncrease}>Increase Counter</button>
    <button onClick={handleReRender}>Re render counter</button>
  </div>)
}
