import { Container, CssBaseline, Stack } from '@mui/material';
import { NavigationBar } from '../components/NavigationBar/NavigationBar';

export const HomePage = ({ children }: { children: JSX.Element }) => {

  return (<>
    <Stack>
      <NavigationBar />
      {children}
    </Stack>
  </>)
}
