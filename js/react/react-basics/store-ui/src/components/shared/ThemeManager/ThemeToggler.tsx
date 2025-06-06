import React from 'react';
import { createTheme, CssBaseline, ThemeProvider } from "@mui/material";
import { useMemo } from "react";
import { selectTheme } from '../../../store/ThemeSlice';
import { useSelector } from 'react-redux';

/**
 * Theme toggler component
 * - handle the selected theme of the application and provides to children components
 * - the component uses redux to handle theme state
 * 
 * @param children - Children component to render inside the theme provider 
 * @component 
 */
export const ThemeToggler = (props: { children: JSX.Element }) => {
    const mode = useSelector(selectTheme)

    const theme = useMemo(
        () =>
            createTheme({
                palette: {
                    mode,
                },
            }),
        [mode],
    );

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            {props.children}
        </ThemeProvider>
    );

}
