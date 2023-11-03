import { styled } from 'styled-components'
import Lottie from 'lottie-react'

export const HomeContainer = styled.div`
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;
`

export const GameCardContainer = styled.div`
  display: flex;
  width: 100%;
  justify-content: space-around;
  top: 90px;
  position: absolute;
  padding-left: 24px;
  padding-right: 24px;
`

export const ButtonLow = styled.div`
  bottom: 14px;
  right: 14px;
  position: absolute;
`

export const BackgroundLottie = styled(Lottie)``
