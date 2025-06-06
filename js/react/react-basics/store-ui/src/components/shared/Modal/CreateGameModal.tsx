import { Box, Button, Grid, GridProps, IconButton, Modal, Rating, styled, TextField, Typography } from "@mui/material"
import { useState } from "react";
import AddIcon from '@mui/icons-material/Add';
import { GameImage } from "../../GamesList/GameItem";
import CheckIcon from '@mui/icons-material/Check';
import CancelIcon from '@mui/icons-material/Cancel';
import { createGame } from "../../../services/GameService";
import { Game } from "../../../model/Game.model";
import { useModalState } from "../../../services/hooks/ModalHooks";
import { useAuth } from "../../../services/hooks/AuthHooks";

interface CreateModalError {
  title?: string,
  price?: string,
  stock?: string
}

// How to use styled components instead of prop style
const style = {
  position: 'absolute' as 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: '50%',
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4,
};

const newGameInitialState = {
  id: '',
  imgUrl: '',
  title: '',
  description: '',
  calification: 0,
  price: 0,
  publishDate: '',
  stock: 0,
  comments: [],
  tags: []
}

const BodyContainer = styled(Grid)<GridProps>(({ theme }) => ({
  padding: '1rem',
  justifyContent: 'space-evenly',
}))

const CalificationContainerStyled = styled(Grid)<GridProps>(({ theme }) => ({
  justifyContent: 'flex-end'
}))

/**
 * Create game modal
 * - Displays a modal with a form to create a modal
 * - Performs form validation and request server-side Game creation
 * 
 * @component
 */
export const CreateGameModal = () => {

  const [open, handleOpen, handleClose] = useModalState();
  const [newGame, setNewGame] = useState<Game>(newGameInitialState)
  const [errors, setErrors] = useState<CreateModalError>({})
  const [user, login, logout, isLogin] = useAuth()

  const handleModalClose = () => {
    handleClose()
    resetForm()
  }

  const handleSubmit = async () => {
    if (formValidation()) {
      try {
        await createGame({ ...newGame, publishDate: new Date().toISOString() })
        handleClose()
      } catch (error: any) {
        console.log(error.data)
      }
    }
  }
  const formValidation = () => {
    let errors = {};
    if (!newGame.title) { errors = { ...errors, title: 'Required' } }
    if (!newGame.price) { errors = { ...errors, price: 'Required' } }
    if (!newGame.stock) { errors = { ...errors, stock: 'Required' } }

    setErrors(errors)
    return Object.keys(errors).length === 0
  }

  const resetForm = () => {
    setNewGame(newGameInitialState)
    setErrors({})
  }

  return <>
    {isLogin() &&
      <IconButton onClick={handleOpen} aria-label="delete">
        <AddIcon />
      </IconButton>
    }
    <Modal
      open={open}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={style}>
        <Grid container>
          <Grid item xs={12}>
            <Typography variant="h4">Create Game</Typography>
          </Grid>
          <BodyContainer container item xs={3}>
            <GameImage squareSize={110} src={newGame.imgUrl} />
          </BodyContainer>
          <BodyContainer container item xs={9}>
            <Grid container item xs={6}>
              <TextField onChange={(event) => setNewGame({ ...newGame, title: event.target.value })}
                id="standard-basic" label="Title" variant="standard"
                required
                helperText={errors.title || false}
                error={!!errors.title} />
            </Grid>
            <CalificationContainerStyled container item xs={6}>
              <Typography component="legend">Calification</Typography>
              <Rating
                name="simple-controlled"
                value={newGame.calification}
                onChange={(event, newValue) => {
                  setNewGame({ ...newGame, calification: newValue || 1 });
                }}
              />
            </CalificationContainerStyled>
            <Grid item xs={12}>
              <TextField onChange={(event) => setNewGame({ ...newGame, imgUrl: event.target.value })} label="Image URL" variant="standard" />
            </Grid>
            <Grid item xs={12}>
              <TextField onChange={(event) => setNewGame({ ...newGame, description: event.target.value })} label="Description" variant="standard" />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                onChange={(event) => setNewGame({ ...newGame, price: Number(event.target.value) })}
                label="Price" variant="standard"
                type='number'
                helperText={errors.price || false}
                error={!!errors.price} />
            </Grid>
            <Grid item xs={12}>
              <TextField
                required
                onChange={(event) => setNewGame({ ...newGame, stock: Number(event.target.value) })}
                label="Stock" variant="standard"
                type='number'
                helperText={errors.stock || false}
                error={!!errors.stock} />
            </Grid>
          </BodyContainer>
          <Grid container item xs={12} justifyContent='space-evenly'>
            <Button onClick={handleModalClose} variant="outlined" startIcon={<CancelIcon />}>
              Cancel
            </Button>
            <Button onClick={handleSubmit} variant="contained" endIcon={<CheckIcon />}>
              Submit
            </Button>
          </Grid>
        </Grid>
      </Box>
    </Modal>
  </>
}
