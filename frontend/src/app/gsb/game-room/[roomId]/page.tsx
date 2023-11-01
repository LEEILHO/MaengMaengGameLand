'use client'

import withAuth from '@components/hoc/client/PrivateRoute'
import React, { useState } from 'react'

import * as S from '@styles/gsb/GameRoom.styled'
import TurnCard from '@components/gsb/client/TurnCard'

const GameRoom = () => {
  // 전광판 하나로 해서 상황에 따라 메세지만 바꾸기
  const [displayMessage, setDisplayMessage] =
    useState<string>('카드를 뒤집어 선공을 정해주세요.')
  return (
    <>
      <S.GameRoomContainer>
        <S.DisplayBoard>{displayMessage}</S.DisplayBoard>
      </S.GameRoomContainer>
      {/* <TurnCard /> */}
    </>
  )
}

export default withAuth(GameRoom)
