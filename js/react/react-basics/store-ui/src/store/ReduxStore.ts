import { configureStore } from "@reduxjs/toolkit";
import ThemeReducer from "./ThemeSlice";
import ShoppingCartReducer from './ShoppingCartSlice'


export const store = configureStore({
  reducer: {
    theme: ThemeReducer,
    shoppingCart: ShoppingCartReducer
  }
})

export type RootState = ReturnType<typeof store.getState>

export type AppDispatch = typeof store.dispatch
