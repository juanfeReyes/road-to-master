import Typography from "@mui/material/Typography"
import StarIcon from '@mui/icons-material/Star';
import StarBorderIcon from '@mui/icons-material/StarBorder';
import StarHalfIcon from '@mui/icons-material/StarHalf';
import VerifiedIcon from '@mui/icons-material/Verified';
import styled from "styled-components";


const Container = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-evenly;
  padding-left: 0.5rem;
`;

export const CalificationHeader = (props: { calification: number }) => {
  const { calification } = props;


  return <>
    <Container>
      {calification <= 2 && <StarBorderIcon/>}
      {calification === 3 && <StarHalfIcon/>}
      {calification === 4 && <StarIcon/>}
      {calification === 5 && <VerifiedIcon/>}
      <Typography> {calification}</Typography>
    </Container>
  </>
}
