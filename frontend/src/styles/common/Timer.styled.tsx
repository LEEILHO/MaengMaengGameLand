import { css, styled } from 'styled-components'

interface TimerProps {
  $size: string
}

interface TimerIconProps {
  $type: 'SHAKE' | 'COMMON'
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

export const TimerBackGround = styled.div<TimerIconProps>`
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
    animation: ${(props) => props.$type === 'SHAKE' && 'shake 0.5s infinite'};

    @keyframes shake {
      0% {
        transform: translateX(0) rotate(5deg);
      }
      25% {
        transform: translateX(-3px) rotate(-5deg);
      }
      50% {
        transform: translateX(3px) rotate(5deg);
      }
      75% {
        transform: translateX(-3px) rotate(-5deg);
      }
      100% {
        transform: translateX(3px) rotate(5deg);
      }
    }
  }
`
