import { Comment } from "./Comment.modal";

/**
 * Game
 * @alias Game
 */
export interface Game {
  /** Game ID */
  id: string;

  /** image url of the game */
  imgUrl: string;

  /** Game title */
  title: string;
  
  /** Game description */
  description: string;

  /** Game rating from 0 to 5 */
  calification: number;

  /** Game price */
  price: number;

  /** Game publish date */
  publishDate: string;

  /** number of games available in stock for rapid delivery */
  stock: number;

  /** List of Comments ids */
  comments: string[]

  /** List of tags */
  tags: string[]

  expand?: { 
    comment?: Comment[],
    recomendedGames?: Game[]
  }
}
