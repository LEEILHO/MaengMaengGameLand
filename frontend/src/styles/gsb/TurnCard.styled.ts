import { styled } from 'styled-components'
import { motion } from 'framer-motion'

export const OrderingCardContainer = styled.div`
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.75);
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`

export const CardList = styled.div`
  display: flex;
  gap: 96px;
`

export const CardContainer = styled(motion.button)`
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  background: none;
  transition: transform 0.4s;
  transform-style: preserve-3d;
  width: 130px;
  height: 183px;
`

export const CardFront = styled(motion.img)`
  position: absolute;
  backface-visibility: hidden;
  transform: rotateY(180deg);
  width: 100%;
  height: 100%;
`

export const CardBack = styled(motion.img)`
  position: absolute;
  backface-visibility: hidden;
  width: 100%;
  height: 100%;
`
