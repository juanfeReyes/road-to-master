import { Button, Grid, IconButton, Paper, styled, TextField } from "@mui/material"
import { useState } from "react"
import { useDispatch } from "react-redux"
import { Game } from "../../model/Game.model"
import { addGame } from "../../store/ShoppingCartSlice"
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import AddIcon from '@mui/icons-material/Add';

const Container = styled(Paper)(() => ({
  width: '100%',
  padding: '0.5rem',
  textAlign: 'center'
}))

const GridContainer = styled(Grid)(() => ({
  padding: '0.5rem',
  justifyContent: 'flex-end'
}))


export const MultipleAddToCart = (props: { label: string, game: Game }) => {
  const { label, game } = props;

  const [itemsToCart, setItemsToCart] = useState(0)
  const dispatch = useDispatch()

  return <>
    <GridContainer id='multiple-add-cart' container>
      <Container elevation={3}>
        <Grid container rowSpacing={2}>
          <Grid item xs={10}>
            <TextField
              fullWidth
              size="small"
              id="multiple-add-to-cart"
              label={label}
              defaultValue={itemsToCart}
              onChange={(event) => setItemsToCart(Number(event.target.value))}
            />
          </Grid>
          <Grid item xs={2}>
            <IconButton
              size="large"
              color="primary"
              aria-label="add-to-cart"
              disabled={itemsToCart < 1 || itemsToCart >= game.stock}
              onClick={() => dispatch(addGame({ game: game, count: itemsToCart }))}>
              <AddIcon />
              <LocalShippingIcon />
            </IconButton>
          </Grid>
        </Grid>
      </Container>
    </GridContainer>
  </>
}
