import { css, styled } from 'styled-components'
import { motion } from 'framer-motion'

type WrrapperProps = {
  $isIos: boolean
}

export const Overlay = styled.div`
  z-index: 10;
  background-color: rgba(0, 0, 0, 0.7);
  position: fixed;
  width: 100%;
  height: 100%;
`

export const Wrapper = styled(motion.div)<WrrapperProps>`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  ${(props) =>
    !props.$isIos &&
    css`
      top: calc(50% - env(keyboard-inset-height, 0));
    `}
`
