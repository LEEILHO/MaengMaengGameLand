import { styled } from 'styled-components'
import Lottie from 'lottie-react'
import { colors } from '@constants/colors'

export const WaitingRoomContainer = styled.div`
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;
`
export const Contents = styled.div`
  display: flex;
  width: 100%;
  position: fixed;
  top: 54px;
  left: 49px;
  gap: 24px;
`

export const PlayerList = styled.div`
  width: 500px;
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  row-gap: 12px;
  column-gap: 8px;
  align-items: center;
  justify-items: center;
`

export const BottomButtons = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
  position: fixed;
  bottom: 0px;
  padding: 0px 20px 12px 20px;
`

export const ButtonRelatedGame = styled.div`
  display: flex;
  gap: 8px;
`

export const MoveBackButton = styled.button`
  display: flex;
  padding: 8px 8px;
  align-items: center;
  justify-content: center;
  transition: 0.25s;
  cursor: pointer;
  border-radius: 100%;
  background: radial-gradient(
      107.08% 85.59% at 86.3% 87.5%,
      rgba(0, 0, 0, 0.23) 0%,
      rgba(0, 0, 0, 0) 86.18%
    ),
    radial-gradient(
      83.94% 83.94% at 26.39% 20.83%,
      rgba(255, 255, 255, 0.41) 0%,
      rgba(255, 255, 255, 0) 69.79%,
      rgba(255, 255, 255, 0) 100%
    ),
    ${colors.button.yellow};
  box-shadow:
    4px 38px 62px 0px rgba(0, 0, 0, 0.5),
    -3px -4px 7px 0px rgba(255, 255, 255, 0.15) inset;

  &:active {
    transform: translateY(4px);
    filter: brightness(0.7);
  }

  img {
    width: 28px;
    height: 28px;
  }
  margin-left: 34px;
`

export const Background = styled(Lottie)``
