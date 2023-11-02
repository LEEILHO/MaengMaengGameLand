'use client'

import withAuth from '@components/hoc/client/PrivateRoute'
import React, { useEffect, useState } from 'react'

import * as S from '@styles/gsb/GameRoom.styled'
import TurnCard from '@components/gsb/client/TurnCard'
import Timer from '@components/common/clients/Timer'
import PlayerCard from '@components/gsb/client/PlayerCard'
import { images } from '@constants/images'
import CombinationGsb from '@components/gsb/client/CombinationGsb'

const GameRoom = () => {
  // 전광판 하나로 해서 상황에 따라 메세지만 바꾸기
  const [displayMessage, setDisplayMessage] =
    useState<string>('카드를 뒤집어 선공을 정해주세요.')

  return (
    <>
      <S.GameRoomContainer>
        <S.TopRow>
          <S.DisplayBoard>{displayMessage}</S.DisplayBoard>
        </S.TopRow>
        <S.CenterRow>
          <PlayerCard nickname="심은진" chipsPlayerHas={30} weight={0} />
          {/* <CombinationGsb /> */}
          <PlayerCard nickname="김진영" chipsPlayerHas={30} weight={0} />
        </S.CenterRow>
      </S.GameRoomContainer>
      {/* <TurnCard /> */}
    </>
  )
}

export default withAuth(GameRoom)
