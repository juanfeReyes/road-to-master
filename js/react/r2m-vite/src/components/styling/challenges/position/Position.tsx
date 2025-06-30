
import { useEffect, useRef, useState, type HTMLProps } from 'react'
import styles from './Position.module.scss'

export const Position = () => {

  return (<div className={`${styles.flex}`}>
    <StaticPosition />
    <AbsolutePosition />
    <FixedPosition />
  </div>)
}

/**
 * Static position is the default
 * Calculates the position based on siblings to follow flow of the DOM
 * 
 * @returns 
 */
const StaticPosition = () => {

  return (<div className={`${styles['parent-box']}`}>
    <h1>Static</h1>
    <Box className={`${styles.box} ${styles['static-position']}`}></Box>
    <Box className={`${styles.box} ${styles['static-position']}`}></Box>
  </div>)
}

/**
 * Absolute position allows to set the location of element based on parent
 * top, left, right botton define the distance between parent border and child border
 * 
 * @returns 
 */
const AbsolutePosition = () => {

  return (<div className={`${styles['parent-box']}`}>
    <h1>Absolute</h1>
    <Box className={`${styles.box} ${styles['absolute-one']}`}></Box>
    <Box className={`${styles.box} ${styles['absolute-two']}`}></Box>
  </div>)
}

/**
 * Absolute position allows to set the location of element based on body of document
 * top, left, right botton define the distance between body DOM border and child border
 * 
 * @returns 
 */
const FixedPosition = () => {

  return (<div className={`${styles['parent-box']}`}>
    <h1>Fixed</h1>
    <Box className={`${styles.box} ${styles['fixed-one']}`}></Box>
    <Box className={`${styles.box} ${styles['fixed-two']}`}></Box>
  </div>)
}

interface BoxProps extends HTMLProps<HTMLElement> {}
const Box = ({className}: BoxProps) => {
  const ref = useRef<HTMLDivElement>(null)
  const [coords, setCoords] = useState({top: 0, left: 0})

  useEffect(() => {
    const aux = ref.current?.getBoundingClientRect()
    if(aux) {
      setCoords({top: aux?.top, left: aux?.left})
    }
  }, [])

  return (<div ref={ref} className={className}>
    top: {coords.top} - left: {coords.left}
  </div>)
}