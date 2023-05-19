import { Badge, IconButton } from "@mui/material"
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import { useSelector } from "react-redux";
import { selectCart } from "../../store/ShoppingCartSlice";
import { ShoppingCartModal } from "../shared/Modal/ShoppingCartModal";
import { useModalState } from "../../services/hooks/ModalHooks";

/**
 * Shopping cart badge
 * - Shows number of items and shows a modal to show a resume Games in the shopping cart
 * 
 * @component
 */
export const ShoppingCartBadge = () => {
  const [open, handleOpen, handleClose] = useModalState();

  const shoppingCart = useSelector(selectCart)

  return <>
    <IconButton onClick={handleOpen} aria-label="cart">
      <Badge badgeContent={shoppingCart.counter} color="secondary">
        <ShoppingCartIcon />
      </Badge>
    </IconButton>
    <ShoppingCartModal open={open} handleClose={handleClose} />
  </>
}
