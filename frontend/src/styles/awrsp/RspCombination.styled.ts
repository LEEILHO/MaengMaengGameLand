import { colors } from '@constants/colors'
import { styled } from 'styled-components'

interface DragProps {
  isDragging: boolean
}

export const Container = styled.div`
  width: 100%;
  height: 100%;
`

export const DragDropContextDiv = styled.div`
  width: 100%;
  height: 100%;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 20px;
`

export const InDropArea = styled.div`
  width: 90%;
  height: 142px;

  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;

  background-color: rgba(217, 217, 217, 0.5);
  border-radius: 8px;

  transform: none !important;
`

export const OutDropArea = styled.div`
  display: flex;
`

export const DragCard = styled.div<DragProps>`
  img {
    width: 64px;
    height: 96px;

    transform: ${(props) => (props.isDragging ? 'scale(1.2)' : '')};
    transition: ${(props) => (props.isDragging ? '0.3s' : '')};
  }

  @media screen and (max-width: 700px) {
    img {
      width: 48px;
      height: 80px;
    }
  }
`

export const EmptyCard = styled.div`
  width: 64px;
  height: 96px;
  background-color: ${colors.greyScale.grey300};
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.4) inset;
`

export const BottomButton = styled.div`
  position: fixed;
  bottom: 20px;
  right: 20px;
`
