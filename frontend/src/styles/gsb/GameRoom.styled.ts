import { images } from '@constants/images'
import { styled } from 'styled-components'

export const GameRoomContainer = styled.div`
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;

  display: flex;
  flex-direction: column;
  justify-content: space-between;

  background-image: url(${images.gsb.background});
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
`

export const TopRow = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  margin-top: 20px;
`

export const DisplayBoard = styled.p`
  width: 50%;
  padding: 16px 54px;
  border-radius: 12px;
  background: rgba(228, 241, 255, 0.75);
  z-index: 3;

  text-align: center;
  font-size: 18px;
  font-weight: 700;
  line-height: normal;
`

export const CenterRow = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0px 40px;
`
