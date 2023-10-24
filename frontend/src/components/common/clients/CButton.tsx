'use client'
import * as S from '@styles/common/button/Button.styled'
import React from 'react'

type Props = React.HTMLProps<HTMLButtonElement> & {
  height?: number
  width?: number
  radius?: number
  text?: string
  fontSize?: number
  color?: string
  backgroundColor: string
}

const CButton = ({
  height,
  width,
  radius,
  color,
  fontSize,
  backgroundColor,
  text,
  ...rest
}: Props) => {
  return (
    <S.Button
      height={height}
      width={width}
      radius={radius}
      backgroundColor={backgroundColor}
      {...rest}
    >
      <S.Text color={color} fontSize={fontSize}>
        {text}
      </S.Text>
    </S.Button>
  )
}

export default CButton
