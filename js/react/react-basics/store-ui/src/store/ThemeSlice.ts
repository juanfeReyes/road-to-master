import { createSlice } from "@reduxjs/toolkit"
import { RootState } from "./ReduxStore"

const initialState : {value: 'light' | 'dark'} = {
  value: 'light'
}

const ThemeSlice = createSlice({
  name: 'theme',
  initialState,
  reducers: {
    toggleTheme(state) {
      state.value = state.value === 'light' ? 'dark' : 'light'
    }
  }
})

export const selectTheme = (state: RootState ) => state.theme.value

export const { toggleTheme } = ThemeSlice.actions

export default ThemeSlice.reducer
