// test-extend.ts
import { test as testBase } from 'vitest'
import { worker } from './browser'

export const test = testBase.extend('worker', {auto: true}, async () => {
  await worker.start()
  worker.resetHandlers()
  return worker
})