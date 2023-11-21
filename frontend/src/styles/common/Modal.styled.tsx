import { styled } from 'styled-components'
import { motion } from 'framer-motion'

export const Overlay = styled.div`
  z-index: 10;
  background-color: rgba(0, 0, 0, 0.7);
  position: fixed;
  width: 100%;
  height: 100%;
`

export const Wrapper = styled(motion.div)`
  position: absolute;
  top: calc(50% - env(keyboard-inset-height, 0));
  left: 50%;
  transform: translate(-50%, -50%);
`
