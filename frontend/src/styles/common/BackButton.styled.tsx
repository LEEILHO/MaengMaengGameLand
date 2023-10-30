import { styled } from 'styled-components'

interface BackButtonProps {
  $size: number
}

export const BackButton = styled.button<BackButtonProps>`
  width: ${(props) => (props.$size ? `${props.$size}px` : '32px')};
  height: ${(props) => (props.$size ? `${props.$size}px` : '32px')};
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 50%;
  background-color: rgba(255, 195, 68, 1);
  box-shadow: 0px 4px 4px 0px rgba(255, 255, 255, 0.4) inset;
`

export const BackButtonImage = styled.img<BackButtonProps>`
  width: ${(props) => (props.$size ? `${props.$size - 8}px` : '24px')};
  height: ${(props) => (props.$size ? `${props.$size - 8}px` : '24px')};
`
