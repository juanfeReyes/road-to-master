import { useState, type ChangeEvent } from "react"
import { Card } from "../../shared/card/Card"
import { useSwitch } from "./useSwitch"
import { useCopyToClipboard } from "./useCopyToClipboard"
import { MdOutlineContentCopy } from "react-icons/md"
import { useDebounce } from "./useDebounce"

export const CustomHook = () => {

  return (<Card title="Custom hooks">
    <SwithComponent />
    <CopyToClipboard />
    <Debounce />
  </Card>)
}

const SwithComponent = () => {

  const [toggle, switchToggle] = useSwitch()

  return (<div className="box_border">
    <h4>Switch hook</h4>
    <p>{toggle ? 'On' : 'Off'}</p>
    <button onClick={switchToggle}>Switch</button>
  </div>)
}

const CopyToClipboard = () => {
  const [value, setValue] = useState('');
  const [copyText, copy] = useCopyToClipboard()
  const [clipboard, setClipboard] = useState('')

  const handleValueChange = (event: ChangeEvent<HTMLInputElement>) => {
    setValue(event.target.value)
  }

  const handleCopyClick = () => {
    copy(value)
    navigator.clipboard.readText().then((text) => {setClipboard(text)})
  }

  return <div className="box_border">
    <h4>Copy clipboard hook</h4>
    <p>{clipboard}</p>
    <input onChange={handleValueChange} /><button onClick={handleCopyClick}><MdOutlineContentCopy /></button>
  </div>
}

const Debounce = () => {
  const [text, setText] = useState('')
  const dText = useDebounce(text, 1000)

  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    setText(event.target.value)
  }

  return (<div className="box_border">
    <p>{dText}</p>
    <input onChange={handleChange} value={text}/>
  </div>)
}
