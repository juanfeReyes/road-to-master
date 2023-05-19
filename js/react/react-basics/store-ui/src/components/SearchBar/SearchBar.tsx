import InputBase from '@mui/material/InputBase';
import Paper from '@mui/material/Paper';
import React, { useState } from 'react';
import SearchIcon from '@mui/icons-material/Search';
import IconButton from '@mui/material/IconButton';
import styled from 'styled-components'

const placeHolder = 'Search games'

const StyledContainer = styled.div`
  padding: 1.5rem;
  flex-wrap: wrap;
  justify-content: end;
`
/**
 * @callback handleSearchGames
 * @param {string} title - title to search the game
 */

/**
 * Search bar to type the title of games to search
 * 
 * @param {handleSearchGames} handleSearchGames - function to search for games
 * @component
 */
export const SearchBar = (props: { handleSearchGames: (title: string) => void }) => {
  const { handleSearchGames } = props;
  const [searchValue, setSearchValue] = useState('')

  const handleSearchEnter = (e: any) => {
    if (e.key === 'Enter'){
      e.preventDefault()
      handleSearchGames(searchValue)
    }
  }

  return <>
    <StyledContainer>
      <Paper
        component="form"
        sx={{ p: '2px 4px', display: 'flex', alignItems: 'center', width: '50%' }}
        elevation={4}>
        <InputBase
          sx={{ ml: 1, flex: 1 }}
          placeholder={placeHolder}
          value={searchValue}
          onChange={(event) => { setSearchValue(event.target.value) }}
          onKeyDown={handleSearchEnter}
        />
        <IconButton type='button' sx={{ p: '10px' }} onClick={() => handleSearchGames(searchValue)}>
          <SearchIcon />
        </IconButton>
      </Paper>
    </StyledContainer>

  </>
}
