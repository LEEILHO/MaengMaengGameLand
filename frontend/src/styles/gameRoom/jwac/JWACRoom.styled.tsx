import { images } from '@constants/images'
import { styled } from 'styled-components'

export const RoomContainer = styled.div`
  background-image: url(${images.gameRoom.jwac.backGround});
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: fixed;
  top: 0;
  bottom: 0;
`

export const RoomResultContainer = styled.div`
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`

export const ItemContainer = styled.div`
  display: flex;
  position: absolute;
  top: 12px;
  right: 24px;
`

export const ItemIcon = styled.img`
  width: 40px;
  height: 40px;
`

export const NewsContainer = styled.div`
  border-radius: 8px;
  background: rgba(0, 0, 0, 0.75);
  box-shadow: 4px 8px 10px 0px rgba(0, 0, 0, 0.25);
  display: flex;
  justify-content: center;
  align-items: center;
  height: 36px;
  margin-top: 12px;
  width: 60%;
`

export const CumulativePrice = styled.p`
  font-size: 14px;
  font-weight: 700;
  color: white;

  @media screen and (max-width: 740px) {
    font-size: 12px;
  }
`

export const DisplayBoardContainer = styled.div`
  flex: 1;
  margin-top: 56px;
  margin-right: 16px;
  width: 60%;
  position: absolute;
  top: 10%;
  right: 25%;
  height: 65%;
`

export const DisplayRoundFrame = styled.img`
  width: 80%;
  height: 66px;
  position: absolute;
  top: -32px;
  left: 50%;
  transform: translateX(-50%);
`

export const RoundText = styled.p`
  font-weight: 700;
  font-size: 18px;
  position: absolute;
  top: -8px;
  left: 50%;
  transform: translateX(-50%);
  color: white;
`

export const NoteContainer = styled.div`
  position: absolute;
  right: -12px;
  top: 20%;
  width: 30%;
  height: 70%;
  background-image: url(${images.gameRoom.jwac.noteFrame});
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  padding: 0 36px;
  display: flex;
  flex-direction: column;

  @media screen and (max-width: 740px) {
    padding: 0 12px;
  }
`

export const Discription = styled.h6`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 700;
  margin-top: 24px;

  @media screen and (max-width: 740px) {
    font-size: 12px;
    margin-top: 28px;
  }
`

export const PriceRow = styled.div`
  display: flex;
  width: 100%;
  margin-top: 24px;
  padding: 0 12px;

  .price-input {
    border: none;
    border-bottom: 1px solid;
    border-radius: 0;
    /* flex: 1; */
    width: 100%;
    font-size: 12px;
    font-weight: 700;
    text-align-last: right;
    margin-right: 4px;
    margin-left: 12px;
    background-color: transparent;
    /* width: 50px; */

    @media screen and (max-width: 740px) {
      margin-right: 2px;
      margin-left: 4px;
    }
  }
`

export const SubmitPrice = styled.div`
  font-size: 14px;
  font-weight: 700;
  width: 100%;
  text-align: right;
`

export const ButtonRow = styled.div`
  display: flex;
  margin-left: auto;
  margin-bottom: 12px;

  @media screen and (max-width: 740px) {
    margin-bottom: 32px;
    margin-right: 12px;
  }
`

export const CurrentPriceRow = styled.div`
  display: flex;
  width: 100%;
  margin-top: 24px;
  padding: 0 12px;
  justify-content: end;
  font-size: 14px;
  font-weight: 700;
`

export const CumlativeAmountCotainer = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 12px;
  font-weight: 700;
  font-size: 14px;
  margin-left: 12px;
  margin-top: auto;
  margin-right: 4px;

  @media screen and (max-width: 740px) {
    font-size: 12px;
    margin-left: 16px;
  }
`

export const CumlativeDiscriptionRow = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  img {
    width: 24px;
    height: 24px;
    margin-right: 4px;
  }
`

export const CumlativePrice = styled.p`
  margin-top: 6px;
  text-align: right;
`

export const PriceInput = styled.input`
  border: none;
  border-bottom: 1px solid;
  border-radius: 0;
  /* flex: 1; */
  width: 90%;
  font-size: 12px;
  font-weight: 700;
  text-align-last: right;
  margin-right: 4px;
  margin-left: 12px;
  background-color: transparent;
  /* width: 50px; */

  @media screen and (max-width: 740px) {
    margin-right: 2px;
    margin-left: 4px;
  }
`

export const PriceUnit = styled.p`
  font-size: 12px;
  font-weight: 700;
  margin-right: 4px;
  margin-top: 2px;
`

export const TimerContainer = styled.div`
  position: absolute;
  top: 24px;
  left: 24px;
`

export const ShowCaseCatainer = styled.div`
  position: absolute;
  left: 32px;
  bottom: 0;
`

export const Table = styled.img`
  position: absolute;
  bottom: 0;
  width: 168px;
  height: 168px;

  @media screen and (max-width: 740px) {
    width: 128px;
    height: 128px;
  }
`

export const JewelContainer = styled.div`
  width: 140px;
  height: 140px;
  position: absolute;
  bottom: 36px;
  left: 16px;

  @media screen and (max-width: 740px) {
    width: 110px;
    height: 110px;
    bottom: 28px;
    left: 10px;
  }
`

export const BackButton = styled.img`
  position: absolute;
  bottom: 12px;
  right: 24px;
  height: 40px;
  width: 40px;
`
