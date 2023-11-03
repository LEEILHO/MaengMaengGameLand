import { styled } from 'styled-components'

interface BarTimerProps extends React.HTMLProps<HTMLDivElement> {
  height?: number
  width?: number
  fontSize?: number
}

export const BarTimerContainer = styled.div<BarTimerProps>`
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 8px;
  background: rgba(228, 241, 255, 0.75);
  box-shadow: 4px 8px 10px 0px rgba(0, 0, 0, 0.25);
  padding: 16px 33px;
  color: #000;
  text-align: center;
  font-size: 18px;
  font-weight: 700;
  line-height: normal;

  gap: 10px;

  position: fixed;
  top: 20px;
  right: 40px;
`
