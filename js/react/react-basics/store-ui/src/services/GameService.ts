import { Game } from "../model/Game.model";
import { pb } from "./AuthService";

const gameCollection = 'games';

/**
 * Service to get games from server
 * 
 * @param filter - filter object
 * @param page - number of page
 * @param limit - size of the page
 * @returns List of games
 */
export const getGames = async (filter: {title: string, tag?: string} = {title: '', tag: ''}, page: number = 0, limit: number = 10) => {
  return (await pb.collection(gameCollection).getList<Game>(page, limit, {filter: `(title~'${filter.title}'&&tags~'${filter.tag? filter.tag :''}')`})).items
}

/**
 * 
 * Service to request information for one item
 * 
 * @param id 
 * @returns a game information
 */
export const getGameDetail = async (id: string) => {
  return (await pb.collection(gameCollection).getOne<Game>(id, {expand: 'comment.author,recomendedGames'}))
}

/**
 * Service to create game in server
 * 
 * @param game - Game to create
 */
export const createGame = async (game: Game)  => {
  await pb.collection(gameCollection).create(game)
}
