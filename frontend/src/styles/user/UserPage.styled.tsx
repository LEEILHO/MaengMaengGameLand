import { styled } from 'styled-components'
import Lottie from 'lottie-react'

export const UesrPageContainer = styled.div`
  display: flex;
  width: 100%;
  height: 100%;
  position: relative;
`

export const BackgroundLottie = styled(Lottie)`
  position: absolute;
  z-index: -1;
`
