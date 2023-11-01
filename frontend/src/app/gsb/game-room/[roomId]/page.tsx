'use client'

import withAuth from '@components/hoc/client/PrivateRoute'
import React from 'react'

import * as S from '@styles/gsb/GameRoom.styled'
import TurnCard from '@components/gsb/client/TurnCard'

const GameRoom = () => {
  return (
    <>
      <S.GameRoomContainer>page</S.GameRoomContainer>
      <TurnCard />
    </>
  )
}

export default withAuth(GameRoom)
