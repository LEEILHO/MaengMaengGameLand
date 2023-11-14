import { styled } from 'styled-components'

import { motion } from 'framer-motion'
import { colors } from '@constants/colors'

interface StarProps {
  index: number
  reverse: string
}

export const RoundResultContainer = styled.div`
  width: 100%;
  height: 100%;

  padding: 26px 60px;

  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-template-rows: repeat(1, 1fr);

  @media screen and (max-width: 700px) {
    padding: 20px 32px;
  }
`

export const CombArea = styled(motion.ul)`
  display: flex;
  flex-direction: column;
  gap: 8px;
`

export const StarList = styled(motion.li)`
  display: flex;
  position: relative;
  height: 37px;
  width: 100%;

  @media screen and (max-width: 700px) {
    height: 30px;
  }
`

export const Star = styled.img<StarProps>`
  width: 40px;
  height: 37px;
  position: absolute;
  left: ${(props) => (props.reverse !== 'true' ? 9 * props.index : '')}px;
  right: ${(props) => (props.reverse === 'true' ? 9 * props.index : '')}px;

  @media screen and (max-width: 700px) {
    width: 33px;
    height: 30px;
  }
`

export const RoundResultModalContainer = styled.div`
  width: 350px;
  height: 150px;
  background-color: ${colors.greyScale.white};

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 8px;

  padding: 0px 16px;

  border-radius: 12px;
`

export const ModalContent = styled.p`
  font-weight: 700;
  word-break: break-all;
`
