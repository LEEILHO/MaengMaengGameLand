import { DragDropContext } from 'react-beautiful-dnd'
import { styled } from 'styled-components'

export const CombinationGsbContainer = styled.div`
  width: 60%;
  display: flex;
  align-items: center;
  justify-content: space-around;
`
export const DragAndDropArea = styled(DragDropContext)`
  display: flex;
  align-items: center;
  justify-content: center;
`
