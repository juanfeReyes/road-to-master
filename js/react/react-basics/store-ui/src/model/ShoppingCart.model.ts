import { Game } from "./Game.model";

/**
 * GameCartItem
 * @alias GameCartItem
 */
export interface GameCartItem extends Game {
  /** number of games in the cart */
  count: number
}
