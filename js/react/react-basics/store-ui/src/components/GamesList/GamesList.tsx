import { styled, Typography } from "@mui/material";
import Container from "@mui/material/Container";
import Grid from "@mui/material/Grid"
import { GameItem } from "./GameItem"
import SentimentDissatisfiedIcon from '@mui/icons-material/SentimentDissatisfied';
import { ListHeader } from "./ListHeader";
import { Game } from "../../model/Game.model";
import { useEffect, useState } from "react";
import { getGames } from "../../services/GameService";
import { SearchBar } from "../SearchBar/SearchBar";
//import { useLoaderData } from "react-router-dom";

const ListContainer = styled(Grid)(() => ({
  paddingTop: "2rem"
}))



/**
 * Component to display a list of Games
 * 
 * @param {Game[]} games - list of games to display 
 * @component
 * @example
 * const games = getGames()
 * return (
 *  <GameList games={games}/>
 * )
 */
export const GamesList = (props : {response: Game[], filters: {[key: string]: string}}) => {
  const [games, setGames] = useState<Game[]>(props.response)
  const [searchFilter, setSearchFilter] = useState<{[key: string]: string}>(props.filters) 

  const handleSearchGames = async (title: string) => {
    const filters = {...searchFilter, title: title}
    const data = await getGames(filters);
    setSearchFilter(filters)
    setGames(data)
  }

  return <>
    <SearchBar handleSearchGames={handleSearchGames} />
    <Container>
      <ListHeader title="Games" filters={searchFilter}/>
      {games.length === 0 && <Typography variant='h3'> <SentimentDissatisfiedIcon fontSize='large' /> No games found</Typography>}
      <ListContainer container spacing={2} rowSpacing={4}>
        {games.map((game) =>
          <Grid item xs={4} key={game.id}>
            <GameItem  {...game}></GameItem>
          </Grid>
        )}
      </ListContainer>
    </Container>
  </>
}
