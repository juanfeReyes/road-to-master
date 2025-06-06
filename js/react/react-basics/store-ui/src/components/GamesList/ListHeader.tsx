import { Chip, ListItem, Paper, Stack, Typography, styled as mstyled } from "@mui/material"
import styled from "styled-components";
import { CreateGameModal } from "../shared/Modal/CreateGameModal";

interface props {
  title: string;
  filters: { [key: string]: string }
}

const ContainerStyled = styled.div`
display: flex;
justify-content: 'end';
`

const PaperStyled = mstyled(Paper)(() => ({
  display: 'flex',
  justifyContent: 'flex-end',
  flexWrap: 'wrap',
  listStyle: 'none',
  padding: '0.5rem'
}))

const StackStyled = mstyled(Stack)(() => ({
  flexDirection: 'row',
  alignItems: 'center',
  'spacing': 1
}))

const FiltersList = (props: { filters: { [key: string]: string } }) => {
  const { filters } = props;
  const areFiltersValid = () => {
    return filters && Object.values(filters).some(value => value)
  }

  return <>
    {
      areFiltersValid() &&
      <PaperStyled
        elevation={1}>
        <StackStyled>
          <Typography>Filters: </Typography>
          {Object.entries(filters).map(filter =>
            filter[1] &&
            <Chip label={`${filter[0].toUpperCase()}:  ${filter[1]}`} />
          )}
        </StackStyled>
      </PaperStyled>
    }
  </>
}

/**
 * 
 * @param props 
 * @returns 
 */
export const ListHeader = (props: props) => {

  const { title } = props;

  return <>
    <Stack>
      <div>
        <ContainerStyled>
          <Typography variant="h3">{title}</Typography>
          <CreateGameModal />
        </ContainerStyled>
      </div>
      <FiltersList filters={props.filters} />
    </Stack>
  </>
}
