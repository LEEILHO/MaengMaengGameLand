import { styled } from 'styled-components'
import Lottie from 'lottie-react'

export const UesrPageContainer = styled.div`
  display: flex;
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;
`

export const LottieContainer = styled.div`
  width: 100%;
  height: 100%;
  position: absolute;
  z-index: -1;
`

export const BackgroundLottie = styled(Lottie)``
