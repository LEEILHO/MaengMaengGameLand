import { DragDropContext } from 'react-beautiful-dnd'
import { styled } from 'styled-components'

export const CombinationGsbContainer = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-around;
  gap: 8px;

  position: relative;
  z-index: 2;
`
export const DragAndDropArea = styled(DragDropContext)`
  display: flex;
  align-items: center;
  justify-content: center;
`

export const SubmitButton = styled.div`
  position: fixed;
  bottom: 20px;
  right: 40px;
`
