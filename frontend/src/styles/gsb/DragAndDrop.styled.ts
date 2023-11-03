import { styled } from 'styled-components'

interface StarProps {
  index: number
  columns: number
}

export const DragAndDropContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 8px;
`

export const InDropArea = styled.div`
  width: 100%;
  height: 201px;
  border-radius: 12px;
  background: rgba(228, 241, 255, 0.75);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;

  .area {
    width: 80%;
    height: 100%;
    position: relative;
  }

  span {
    text-transform: uppercase;
    font-weight: 700;
    margin-top: 16px;
  }

  @media screen and (max-width: 700px) {
    height: 160px;
  }
`

export const OutDropArea = styled.div`
  display: flex;
  width: 100%;
  position: relative;
`

export const Star = styled.img<StarProps>`
  width: 40px;
  height: 37px;
  position: absolute;
  top: ${(props) => 30 * Math.floor(props.index / props.columns)}px;
  left: ${(props) => 10 * (props.index % props.columns)}px;

  @media screen and (max-width: 700px) {
    top: ${(props) => 30 * Math.floor(props.index / props.columns)}px;
    left: ${(props) => 8 * (props.index % props.columns)}px;
    width: 33px;
    height: 30px;
  }
`
