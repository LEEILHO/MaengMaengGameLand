import { css, styled } from 'styled-components'

interface TimerProps {
  $size: string
}

export const TimerContainer = styled.div<TimerProps>`
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  height: ${(props) => props.$size + 'px'};
  width: ${(props) => props.$size + 'px'};

  .rc-progress-circle {
    z-index: 1;
    width: 100%;
    height: 100%;
  }
`

export const TimerBackGround = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: absolute;
  background-color: rgba(15, 15, 15, 0.8);
  width: 90%;
  height: 90%;
  border-radius: 50%;

  img {
    width: 32px;
    height: 32px;
    margin-bottom: 12px;
  }
`
