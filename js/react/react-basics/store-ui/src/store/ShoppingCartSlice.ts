import { createSlice, PayloadAction } from "@reduxjs/toolkit"
import { Game } from "../model/Game.model"
import { GameCartItem } from "../model/ShoppingCart.model"
import { RootState } from "./ReduxStore"

export interface ShoppingCartSliceState {
  games: {[key: string]: GameCartItem}
  counter: number
}

const initialState: ShoppingCartSliceState  = {
  games: {},
  counter: 0
}

const ShoppingCartSlice = createSlice({
  name: 'shoppingCart',
  initialState,
  reducers: {
    addGame(state, gameAction: PayloadAction<{game:Game, count?: number}>) {
      const game = gameAction.payload.game
      const count = gameAction.payload.count ? gameAction.payload.count : 1;
      if(state.games[game.id]) {
        state.games[game.id].count+= count
      }else {
        state.games[game.id] = { ...game, count: count }
      }
      state.counter+= count
    }, 
    removeGame(state, idAction: PayloadAction<string>){
      state.games[idAction.payload].count--
      state.games[idAction.payload].count === 0 && delete state.games[idAction.payload]
      state.counter--
    }
  }
})

export const selectCart = (state: RootState) => state.shoppingCart

export const {addGame, removeGame} = ShoppingCartSlice.actions

export default ShoppingCartSlice.reducer
