import { Button, Grid, IconButton, Typography } from "@mui/material"
import DeleteIcon from '@mui/icons-material/Delete';
import { removeGame } from "../../store/ShoppingCartSlice";
import { useDispatch } from "react-redux";
import { GameCartItem } from "../../model/ShoppingCart.model";

/**
 * Component to display a game added to the shopping cart
 * 
 * @param {GameCartItem} props  - Game cart item to store the game info and number of games in the cart
 * @component 
 */
export const GameItemCart = (props: GameCartItem) => {
  const { id, title, count } = props;
  const dispatch = useDispatch()


  return <>
    <Grid container item xs={12} alignItems='center'>
      <Grid item xs={5}>
        <Typography>{title}</Typography>
      </Grid>
      <Grid item xs={1}>
        <Typography>{count}</Typography>
      </Grid>
      <Grid item xs={5}>
        <Button>View details</Button>
      </Grid>
      <Grid item xs={1}>
        <IconButton onClick={() => dispatch(removeGame(id))} aria-label="delete">
          <DeleteIcon />
        </IconButton>
      </Grid>
    </Grid>

  </>
}
