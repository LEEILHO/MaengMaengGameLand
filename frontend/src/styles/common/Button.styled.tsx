'use client'

import React from 'react'
import styled from 'styled-components'

interface ButtonProps extends React.HTMLProps<HTMLButtonElement> {
  height?: number
  width?: number
  radius?: number
  backgroundColor: string
}

interface TextProps {
  color?: string
  fontSize?: number
}

export const Button = styled.button<ButtonProps>`
  text-align: center;
  font-size: 1.3rem;
  width: ${(props) => (props.height ? props.height + 'px' : '')};
  height: ${(props) => (props.height ? props.height + 'px' : '')};
  display: flex;
  padding: 14px 18px;
  align-items: center;
  justify-content: center;
  transition: 0.25s;
  cursor: pointer;
  border-radius: ${(props) => (props.radius ? props.radius + 'px' : '')};
  background: radial-gradient(
      107.08% 85.59% at 86.3% 87.5%,
      rgba(0, 0, 0, 0.23) 0%,
      rgba(0, 0, 0, 0) 86.18%
    ),
    radial-gradient(
      83.94% 83.94% at 26.39% 20.83%,
      rgba(255, 255, 255, 0.41) 0%,
      rgba(255, 255, 255, 0) 69.79%,
      rgba(255, 255, 255, 0) 100%
    ),
    ${(props) => (props.backgroundColor ? props.backgroundColor : '')};
  box-shadow:
    4px 38px 62px 0px rgba(0, 0, 0, 0.5),
    -3px -4px 7px 0px rgba(255, 255, 255, 0.15) inset;

  &:active {
    transform: translateY(4px);
    filter: brightness(0.7);
  }
`

export const Text = styled.p<TextProps>`
  font-weight: 700;
  font-family: 'kbo-dia';
  text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.25);
  color: ${(props) => (props.color ? props.color : '')};
  font-size: ${(props) => (props.fontSize ? props.fontSize + 'px' : '')};
`
