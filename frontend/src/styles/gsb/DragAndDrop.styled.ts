import { styled } from 'styled-components'

interface StarProps {
  index: number
}

interface DropAreaProps {
  isIn: boolean
}

export const DragAndDropContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`

export const DropArea = styled.div<DropAreaProps>`
  width: 100%;
  height: 201px;
  border-radius: 12px;
  background: ${(props) => (props.isIn ? 'rgba(228, 241, 255, 0.75)' : '')};
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;

  .area {
    width: 80%;
    height: 80%;
    position: relative;
  }

  span {
    text-transform: uppercase;
    font-weight: 700;
    margin-top: 16px;
  }
`

export const Star = styled.img<StarProps>`
  width: 40px;
  height: 37px;
  position: absolute;
  top: ${(props) => 30 * Math.floor(props.index / 10)}px;
  left: ${(props) => 10 * (props.index % 10)}px;
`
