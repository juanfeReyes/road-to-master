import { Grid, Paper, styled, Typography } from "@mui/material"
import { Comment } from "../../model/Comment.modal"
import { CalificationHeader } from "../GamesList/CalificationHeader"

const ItemContainer = styled(Paper)(() => ({
  width: '100%',
  margin: '0.3rem',
  padding: '0.5rem',
  textAlign: 'left'
}))

const ListContainer =  styled(Grid)(() => ({
  padding: '1rem',
  overflowY: 'auto'
}))

const HeaderStyled = styled(Typography)(() => ({
  textAlign: 'left',
  padding: '0.5rem',
  paddingTop: '2rem'
}))

const CommentItem = (props: Comment ) => {
  const {text, rating, expand} = props

  return <>
    <ItemContainer elevation={4}>
      <Grid container item rowSpacing={2} xs={12}>
        <Grid item xs={12}>
          {text}
        </Grid>
        <Grid container item xs={12}>
          <Grid container xs={9}><b>{expand.author.username}</b></Grid>
          <Grid xs={3}><CalificationHeader calification={rating} /></Grid>
        </Grid>
      </Grid>
    </ItemContainer>
  </>
}

export const CommentList = (props: {comments: Comment[]}) => {
  const {comments} = props;

  return <>
  <HeaderStyled variant="h5">Comments</HeaderStyled>
  <ListContainer container spacing={2} rowSpacing={2}>
    {comments.length > 0 ?
     comments.map((comment) => <CommentItem key={comment.id} {...comment}/>) :
     <div>No comments yet! Be the first one</div>
    }
  </ListContainer>
  </>
}
