import { colors } from '@constants/colors'
import { styled } from 'styled-components'

export const DrawCardModalContainer = styled.div`
  width: 504px;
  height: 252px;
  border-radius: 8px;
  background: rgba(79, 79, 79, 0.85);
  box-shadow: 4px 8px 10px 0px rgba(0, 0, 0, 0.25);

  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;

  padding: 10px;
`

export const TopRow = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
`

export const Title = styled.h6`
  color: #fff;
  text-align: center;
  text-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  font-size: 20px;
  font-weight: 700;
  line-height: normal;
  letter-spacing: -0.4px;
`

export const CardListContainer = styled.div`
  width: 100%;

  display: flex;
  justify-content: center;
  align-items: center;

  gap: 66px;
`

export const DrawCard = styled.button`
  background: none;

  img {
    width: 83px;
    height: 116px;
  }

  &:active {
    transform: scale(1.2);
    transition: 0.3s;
  }
`

export const BottomRow = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
`

export const Description = styled.p`
  color: #fff;
  text-align: center;
  text-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  font-size: 12px;
  font-weight: 700;
  line-height: normal;
  letter-spacing: -0.24px;
`

export const SkipButton = styled.button`
  position: absolute;
  right: 0px;

  width: 50px;
  height: 20px;
  font-size: 12px;
  font-weight: 700;
  text-transform: uppercase;
  color: ${colors.primary[100]};
  background: none;
`
