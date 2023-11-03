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
  margin: 20px 0px;
  z-index: 3;

  @media screen and (max-width: 700px) {
    margin: 20px 0px 35px 0px;
  }
`

export const DisplayBoard = styled.p`
  width: 50%;
  padding: 16px 45px;
  border-radius: 12px;
  background: rgba(228, 241, 255, 0.75);

  text-align: center;
  font-size: 18px;
  font-weight: 700;
  line-height: normal;

  @media screen and (max-width: 700px) {
    font-size: 16px;
  }
`

export const CenterRow = styled.div`
  display: flex;
  justify-content: space-between;
  padding: 0px 40px;
`

export const Content = styled.div`
  flex: 1;
  padding: 6px;
`
