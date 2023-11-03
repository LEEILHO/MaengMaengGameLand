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
