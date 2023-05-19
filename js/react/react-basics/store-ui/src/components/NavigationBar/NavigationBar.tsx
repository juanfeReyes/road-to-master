import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import SportsEsportsIcon from '@mui/icons-material/SportsEsports';
import { ThemeSwitch } from '../shared/ThemeManager/ThemeSwitch';
import { ShoppingCartBadge } from './ShoppingCartBadge';
import { useAuth } from '../../services/hooks/AuthHooks';
import { AccountCircle } from '@mui/icons-material';
import { useRouter } from 'next/router';

const settings = ['Profile'];
const title = 'Stream Game Store'

const ProfileButton = (props: any) => {
  const [user, login, logout, isLogin] = useAuth()
  const { handleOpenUserMenu, handleCloseUserMenu, anchorElUser } = props;
  const router = useRouter()

  const goToLoginPage = () => {
    router.push('/login')
  }

  return <>
    <Box sx={{ flexGrow: 0 }}>
      <Tooltip title="Open settings">
        <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
          {isLogin() ? <Avatar alt={user.record.name.toUpperCase()} src="/static/images/avatar/3.jpg" /> : <AccountCircle />}
        </IconButton>
      </Tooltip>
      <Menu
        sx={{ mt: '45px' }}
        id="menu-appbar"
        anchorEl={anchorElUser}
        anchorOrigin={{
          vertical: 'top',
          horizontal: 'right',
        }}
        keepMounted
        transformOrigin={{
          vertical: 'top',
          horizontal: 'right',
        }}
        open={Boolean(anchorElUser)}
        onClose={handleCloseUserMenu}
      >
        {settings.map((setting) => (
          // Enable menu items when developed
          <MenuItem disabled key={setting} onClick={handleCloseUserMenu}>
            <Typography textAlign="center">{setting}</Typography>
          </MenuItem>
        ))}
        {
          isLogin() ?
            <MenuItem key={'Log out'} onClick={logout}>
              <Typography textAlign="center">{'Log out'}</Typography>
            </MenuItem> :
            <MenuItem key={'Login'} onClick={goToLoginPage}>
              <Typography textAlign="center">{'Login'}</Typography>
            </MenuItem>
        }
        <ThemeSwitch />
      </Menu>
    </Box>
  </>
}

/**
 *  Navigation bar to contain menu, navigation options and home button
 * 
 * @component
 */
export function NavigationBar() {
  const [anchorElNav, setAnchorElNav] = React.useState<null | HTMLElement>(null);
  const [anchorElUser, setAnchorElUser] = React.useState<null | HTMLElement>(null);

  const handleOpenNavMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElNav(event.currentTarget);
  };
  const handleOpenUserMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseNavMenu = () => {
    setAnchorElNav(null);
  };

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };


  return (
    <AppBar position='sticky'>
      <Container maxWidth="xl">
        <Toolbar disableGutters>
          <SportsEsportsIcon sx={{ display: { xs: 'none', md: 'flex' }, mr: 1 }} />
          <Typography
            variant="h6"
            noWrap
            component="a"
            href="/"
            sx={{
              mr: 2,
              display: { xs: 'none', md: 'flex' },
              fontFamily: 'monospace',
              fontWeight: 700,
              letterSpacing: '.3rem',
              color: 'inherit',
              textDecoration: 'none',
            }}
          >
            {title}
          </Typography>


          <SportsEsportsIcon sx={{ display: { xs: 'flex', md: 'none' }, mr: 1 }} />
          <Typography
            variant="h5"
            noWrap
            component="a"
            href=""
            sx={{
              mr: 2,
              display: { xs: 'flex', md: 'none' },
              flexGrow: 1,
              fontFamily: 'monospace',
              fontWeight: 700,
              letterSpacing: '.3rem',
              color: 'inherit',
              textDecoration: 'none',
            }}
          >
            {title}
          </Typography>
          <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
          </Box>

          <ShoppingCartBadge />
          <ProfileButton handleOpenUserMenu={handleOpenUserMenu} handleCloseUserMenu={handleCloseUserMenu} anchorElUser={anchorElUser} />
        </Toolbar>
      </Container>
    </AppBar>
  );
}
