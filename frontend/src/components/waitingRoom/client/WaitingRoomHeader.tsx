'use client'

import React from 'react'
import * as S from '@styles/waitingRoom/Header.styled'
import { images } from '@constants/images'

type Props = {
  publicRoom: boolean
  title: string
}

const WaitingRoomHeader = ({ publicRoom, title }: Props) => {
  return (
    <S.HeaderContainer>
      <S.RoomInfo>
        {publicRoom ? (
          <img src={images.waitingRoom.header.unlock} alt="공개방" />
        ) : (
          <img src={images.waitingRoom.header.lock} alt="비공개방" />
        )}
        <p>{title}</p>
      </S.RoomInfo>
      <S.SettingButton>
        <img src={images.common.header.setting} alt="setting" />
      </S.SettingButton>
    </S.HeaderContainer>
  )
}

export default WaitingRoomHeader
