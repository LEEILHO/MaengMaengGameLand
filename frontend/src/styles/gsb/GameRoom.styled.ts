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
  padding: 0px 6px;
  position: relative;
`

export const BottomRow = styled.div`
  width: 100%;
  position: fixed;
  bottom: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`

export const ManyChips = styled.img`
  position: absolute;
  width: 450px;
  height: 118px;
  bottom: -30;
`

export const AllBetChips = styled.p`
  width: 364px;
  height: 50px;
  border-radius: 12px;
  background: rgba(228, 241, 255, 0.9);

  font-weight: 700;
  font-size: 24px;

  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
  margin-bottom: 20px;
`

export const BackButton = styled.img`
  position: absolute;
  bottom: 12px;
  right: 24px;
  height: 40px;
  width: 40px;
`
