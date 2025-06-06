import { Box, Grid, Modal, Typography } from "@mui/material";
import { useSelector } from "react-redux";
import { selectCart } from "../../../store/ShoppingCartSlice";
import { GameItemCart } from "../../ShoppingCart/GameItemCart";
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';


interface ShoppingCartModalProps {
  open: boolean;
  handleClose: () => void
}

const style = {
  position: 'absolute' as 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4,
};

/**
 * Modal to show the list of games added to the shopping cart
 * 
 * @param {boolean} open - flag to open or close modal
 * @param handleClose - function to open or close the modal
 * 
 * @component
 *  
 */
export const ShoppingCartModal = (props: ShoppingCartModalProps) => {
  const { open, handleClose } = props;
  const shoppingCart = useSelector(selectCart)

  return <>
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={style}>
        <Grid container alignItems='center'>
          <Grid item xs={1}>
            <ShoppingCartIcon />
          </Grid>
          <Grid item xs={11}>
            <Typography variant="h5">Shopping Cart</Typography>
          </Grid>
        </Grid>
        {shoppingCart.counter === 0 ? <Typography> Shopping cart is empty</Typography> :
          <Grid container >
            {Object.values(shoppingCart.games).map(game => {
              return <>
                <GameItemCart {...game} />
              </>
            })}
          </Grid>
        }
      </Box>
    </Modal>
  </>
}
