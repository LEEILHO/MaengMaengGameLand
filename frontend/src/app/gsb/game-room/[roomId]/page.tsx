'use client'

import withAuth from '@components/hoc/client/PrivateRoute'
import React from 'react'

import * as S from '@styles/gsb/GameRoom.styled'
import OrderingCard from '@components/gsb/client/OrderingCard'

const GameRoom = () => {
  return (
    <>
      <S.GameRoomContainer>page</S.GameRoomContainer>
      <OrderingCard />
    </>
  )
}

export default withAuth(GameRoom)
