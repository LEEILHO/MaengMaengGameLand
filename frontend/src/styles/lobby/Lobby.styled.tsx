import { styled } from 'styled-components'
import Lottie from 'lottie-react'

export const LobbyContainer = styled.div`
  width: 100%;
  height: 100%;
`

export const BackgroundLottie = styled(Lottie)``

export const Title = styled.h1`
  font-size: 24px;
  font-weight: 700;
  color: white;
  margin: 24px 0 24px 72px;
  position: absolute;
  top: 0;
  left: 0;
`

export const RoomListContainer = styled.div`
  width: 100%;
  height: calc(100vh - 160px);
  display: flex;
  align-items: center;
  position: absolute;
  top: 20%;
`

export const ButtonRow = styled.div`
  display: flex;
  width: 100%;
  justify-content: space-between;
  padding: 0 20px 12px 20px;
  position: absolute;
  bottom: 0;
  left: 0;
`
