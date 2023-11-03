'use client'

import withAuth from '@components/hoc/client/PrivateRoute'
import React, { useEffect, useState } from 'react'

import * as S from '@styles/gsb/GameRoom.styled'
import TurnCard from '@components/gsb/client/TurnCard'
import Timer from '@components/common/clients/Timer'
import PlayerCard from '@components/gsb/client/PlayerCard'
import { images } from '@constants/images'
import CombinationGsb from '@components/gsb/client/CombinationGsb'
import Betting from '@components/gsb/client/Betting'
import BettingStatus from '@components/gsb/client/BettingStatus'
import RoundResult from '@components/gsb/client/RoundResult'

const GameRoom = () => {
  // 전광판 하나로 해서 상황에 따라 메세지만 바꾸기
  const [displayMessage, setDisplayMessage] =
    useState<string>('금은동 조합을 공개합니다')

  const [minBet, setMinBet] = useState<number>(1)
  return (
    <S.GameRoomContainer>
      <S.TopRow>
        <S.DisplayBoard>{displayMessage}</S.DisplayBoard>
      </S.TopRow>
      <S.CenterRow>
        <PlayerCard nickname="심은진" chipsPlayerHas={30} weight={0} />
        <S.Content>
          {/* <CombinationGsb /> */}
          {/* <BettingStatus betChips={[3, 3]} /> */}
          <RoundResult />
        </S.Content>
        <PlayerCard nickname="김진영" chipsPlayerHas={30} weight={0} />
      </S.CenterRow>
      {/* 베팅라운드이고 내가 베팅할 차례일 때 활성화 */}
      {/* <Betting minBet={minBet} chipsPlayerHas={30} /> */}
      {/* <TurnCard /> */}
      <S.BottomRow>
        <S.ManyChips src={images.gsb.allChipsBet} />
        <S.AllBetChips>10</S.AllBetChips>
      </S.BottomRow>
    </S.GameRoomContainer>
  )
}

export default withAuth(GameRoom)
