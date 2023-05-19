import { AccountCircle, Visibility, VisibilityOff } from "@mui/icons-material"
import { Button, FormControl, Grid, IconButton, InputAdornment, InputLabel, OutlinedInput, TextField, Typography, styled } from "@mui/material"
import { Container } from "@mui/system"
import { useState } from "react"
import { useAuth } from "../services/hooks/AuthHooks"
import StorefrontIcon from '@mui/icons-material/Storefront';
import ReportIcon from '@mui/icons-material/Report';

const LoginContainer = styled(Container)(() => ({
  justifyContent: 'center',
  paddingTop: '5rem'
}))

const GridCenterStyled = styled(Grid)(() => ({
  justifyContent: 'center',
  alignItems: 'center'
}))


const LoginPage = () => {
  const [user, login] = useAuth()
  const [userForm, setUserForm] = useState({ username: '', password: '', error: false, errorMessage: '' })

  const [showPassword, setShowPassword] = useState(false);
  const handleClickShowPassword = () => setShowPassword((show) => !show);
  const handleMouseDownPassword = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault();
  };

  const handleLogin = async () => {
    try {
      await login(userForm.username, userForm.password)
    } catch (e: any) {
      setUserForm({ ...userForm, error: true, errorMessage: e.message })
    }
  }

  const handleType = (field: string, value: string) => {
    setUserForm({ ...userForm, [field]: value })
  }

  return <>
    <LoginContainer>
      <GridCenterStyled container rowSpacing={1}>
        <Grid item sm={12}><Typography variant="h2"><StorefrontIcon fontSize='large'/> Login</Typography></Grid>
        {userForm.error &&
          <GridCenterStyled item sm={12}>
            <Typography color={'red'}><ReportIcon /> {userForm.errorMessage}</Typography>
          </GridCenterStyled>
        }
        <Grid item sm={12}>
          <TextField
            id="user-name-input"
            label="User name"
            value={userForm.username}
            onChange={(event) => handleType('username', (event.target as HTMLInputElement).value)}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <AccountCircle />
                </InputAdornment>
              ),
            }}
            variant="outlined"
          />
        </Grid>
        <Grid item sm={12}>
          <FormControl variant="outlined" onChange={(event) => handleType('password', (event.target as HTMLInputElement).value)}>
            <InputLabel htmlFor="outlined-adornment-password">Password</InputLabel>
            <OutlinedInput
              id="outlined-adornment-password"
              type={showPassword ? 'text' : 'password'}
              value={userForm.password}
              endAdornment={
                <InputAdornment position="end">
                  <IconButton
                    aria-label="toggle password visibility"
                    onClick={handleClickShowPassword}
                    onMouseDown={handleMouseDownPassword}
                    edge="end"
                  >
                    {showPassword ? <VisibilityOff /> : <Visibility />}
                  </IconButton>
                </InputAdornment>
              }
              label="Password"
            />
          </FormControl>
        </Grid>
        <Grid item sm={12}>
          <Button onClick={handleLogin}>Login</Button>
        </Grid>
      </GridCenterStyled>
    </LoginContainer>
  </>
}

export default LoginPage
