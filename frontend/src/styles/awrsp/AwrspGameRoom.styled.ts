import { images } from '@constants/images'
import { styled } from 'styled-components'

export const AwrspGameRoomContainer = styled.div`
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;

  display: flex;
  justify-content: center;
  align-items: center;

  background: url(${images.awrsp.background});
  background-repeat: no-repeat;
  background-size: cover;
`

export const RoundDisplay = styled.div`
  position: fixed;
  top: 5%;
  left: 50%;
  transform: translate(-50%, 0%);

  width: 203px;
  height: 35px;

  display: flex;
  justify-content: center;
  align-items: center;

  color: #fff;
  font-size: 16px;
  font-weight: 700;
  line-height: normal;

  background: url(${images.awrsp.roundDisplay});
  background-repeat: no-repeat;
  background-size: cover;
`

export const Content = styled.div`
  width: 70%;
  height: 80%;
  border-radius: 8px;
  background: rgba(25, 25, 25, 0.75);
  box-shadow: 4px 8px 10px 0px rgba(0, 0, 0, 0.25);

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`

export const TimerContainer = styled.div`
  position: fixed;
  top: 20px;
  right: 20px;
`

export const HistoryButton = styled.button`
  width: 44px;
  height: 44px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  background-color: rgba(255, 195, 68, 1);
  box-shadow: 0px 4px 4px 0px rgba(255, 255, 255, 0.4) inset;

  position: fixed;
  bottom: 20px;
  left: 40px;

  img {
    width: 28px;
    height: 28px;
  }
`

export const BackToLobbyButton = styled.button`
  width: 44px;
  height: 44px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  background-color: rgba(255, 195, 68, 1);
  box-shadow: 0px 4px 4px 0px rgba(255, 255, 255, 0.4) inset;

  position: fixed;
  bottom: 20px;
  right: 40px;

  img {
    width: 28px;
    height: 28px;
    rotate: calc(180deg);
  }
`

export const GameResultContainer = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;

  margin-top: 12px;
`

export const TableHeader = styled.div`
  width: 400px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  color: #fff;
  text-align: center;
  font-size: 16px;
  font-style: normal;
  font-weight: 700;
  line-height: normal;

  margin-bottom: 12px;

  .rank,
  .point {
    width: 100px;
  }
`

export const GameResultList = styled.div`
  width: 100%;
  height: 100px;

  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;

  overflow: auto;
`

export const GameResultItem = styled.div`
  width: 400px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  color: #fff;
  text-align: center;
  font-size: 16px;
  font-style: normal;
  font-weight: 700;
  line-height: normal;

  margin-bottom: 8px;

  .rank,
  .point {
    width: 100px;
  }

  .nickname {
    color: #f5e340;
  }
`

export const AnswerContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 6px;
  margin-top: 25px;
`

export const AnswerTitle = styled.p`
  display: flex;
  align-items: center;
  justify-content: center;

  color: #fff;
  text-align: center;
  font-size: 16px;
  font-style: normal;
  font-weight: 700;
  line-height: normal;
`

export const AnswerList = styled.div`
  border-radius: 8px;
  background-color: rgba(217, 217, 217, 0.5);

  padding: 10px;

  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
`

export const RspCard = styled.img`
  width: 52px;
  height: 75px;
`
