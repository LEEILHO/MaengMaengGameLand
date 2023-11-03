import { css, styled } from 'styled-components'

interface RoomTypeButtonProps {
  $type: 'ACTIVE' | 'INACTIVE'
}

export const RoomTypeButtonContainer = styled.div``

export const RoomTypeButton = styled.button<RoomTypeButtonProps>`
  text-align: center;
  border-radius: 12px 12px 0px 0px;
  background: #332067;
  box-shadow: 0px 4px 4px 0px rgba(255, 255, 255, 0.25) inset;
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
  font-weight: 700;
  width: 64px;
  height: 28px;
  padding-top: 4px;

  ${(props) =>
    props.$type === 'ACTIVE' &&
    css`
      background: #322e2e;
      color: rgba(255, 255, 255, 0.9);
    `}
`
