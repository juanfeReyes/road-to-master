import { StateHook } from "./types/StateHook"
import './Hooks.css'
import { EffectHook } from "./types/EffectHook"
import { MemoHook } from "./types/MemoHook"
import { CallbackHook } from "./types/CallbackHook"
import { ReducerHook } from "./types/ReducerHook"
import { ContextHook } from "./types/ContextHook"

export const Hooks = () => {

  return (<div className="container">
    <StateHook />
    <EffectHook />
    <MemoHook />
    <CallbackHook />
    <ReducerHook />
    <ContextHook />
  </div>)
}
