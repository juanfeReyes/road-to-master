import { FormControlLabel, Switch } from "@mui/material"
import FormGroup from "@mui/material/FormGroup"
import { useTheme } from "@mui/material/styles";
import React from "react";
import Brightness5Icon from '@mui/icons-material/Brightness5';
import DarkModeIcon from '@mui/icons-material/DarkMode';
import styled from "styled-components";
import { toggleTheme } from "../../../store/ThemeSlice";
import { useDispatch } from "react-redux";


const Container = styled.div`
  padding-right: 0.5rem;
`
/**
 * Switch component to toggle between light or dark theme in the application
 * - The component uses redux store to handle the theme 
 * 
 * @component
 */
export const ThemeSwitch = () => {

  const dispatch = useDispatch()
  const theme = useTheme()

  return <>
    <Container>
      <FormGroup>
        <FormControlLabel control={
          <Switch onClick={() => dispatch(toggleTheme())} />
        } label={theme.palette.mode === 'light' ? < Brightness5Icon /> : <DarkModeIcon />} labelPlacement="start" />
      </FormGroup>
    </Container>
  </>
}
