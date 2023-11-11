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
