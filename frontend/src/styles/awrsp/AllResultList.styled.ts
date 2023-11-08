import { styled } from 'styled-components'

export const AllResultListContainer = styled.div`
  width: 100%;
  height: 100%;

  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-template-rows: repeat(2, 1fr);
  gap: 16px;

  padding: 40px 80px;
`
