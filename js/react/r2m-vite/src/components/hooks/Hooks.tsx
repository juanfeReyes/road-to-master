import { StateHook } from "./basics/StateHook"
import './Hooks.css'
import { EffectHook } from "./basics/EffectHook"
import { MemoHook } from "./basics/MemoHook"
import { CallbackHook } from "./basics/CallbackHook"
import { ReducerHook } from "./basics/ReducerHook"
import { ContextHook } from "./basics/ContextHook"
import { CustomHook } from "./custom/CustomHook"

export const Hooks = () => {

  return (<div className="container">
    <StateHook />
    <EffectHook />
    <MemoHook />
    <CallbackHook />
    <ReducerHook />
    <ContextHook />
    <CustomHook />
  </div>)
}
