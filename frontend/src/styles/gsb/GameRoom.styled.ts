import { images } from '@constants/images'
import { styled } from 'styled-components'

export const GameRoomContainer = styled.div`
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;

  background-image: url(${images.gsb.background});
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
`

export const DisplayBoard = styled.div`
  width: 60%;
  padding: 16px 54px;
  border-radius: 12px;
  background: rgba(228, 241, 255, 0.75);

  position: fixed;
  top: 20px;
  left: 50%;
  transform: translate(-50%, 0%);
  z-index: 3;

  text-align: center;
  font-size: 18px;
  font-weight: 700;
  line-height: normal;
`
