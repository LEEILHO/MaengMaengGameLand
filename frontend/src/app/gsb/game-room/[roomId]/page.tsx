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
import BarTimer from '@components/common/clients/BarTimer'
import useSocketGsb from '@hooks/useSocketGsb'
import { usePathname } from 'next/navigation'
import { useRecoilState, useRecoilValue } from 'recoil'
import { DisplayMessageState, RoundState } from '@atom/gsbAtom'

const GameRoom = () => {
  const {
    connectSocket,
    disconnectSocket,
    connectGsb,
    disconnectGsb,
    handleChoiceTurnCard,
  } = useSocketGsb()
  // const gameCode = usePathname().split('/')[3]

  // 전광판 하나로 해서 상황에 따라 메세지만 바꾸기
  const displayMessage = useRecoilValue(DisplayMessageState)

  const [round, setRound] = useRecoilState(RoundState)

  const [minBet, setMinBet] = useState<number>(1)

  useEffect(() => {
    setRound('ChoiceTurn')
    connectSocket(connectGsb, disconnectGsb)
    return () => {
      disconnectSocket()
    }
  }, [])

  return (
    <S.GameRoomContainer>
      <S.TopRow>
        <S.DisplayBoard>{displayMessage}</S.DisplayBoard>
        <BarTimer time={10} />
      </S.TopRow>
      <S.CenterRow>
        <PlayerCard nickname="심은진" chipsPlayerHas={30} weight={0} />
        <S.Content>
          {/* <CombinationGsb /> */}
          {/* <BettingStatus betChips={[3, 3]} /> */}
          {/* <RoundResult /> */}
        </S.Content>
        <PlayerCard nickname="김진영" chipsPlayerHas={30} weight={0} />
      </S.CenterRow>
      {/* 베팅라운드이고 내가 베팅할 차례일 때 활성화 */}
      {/* <Betting minBet={minBet} chipsPlayerHas={30} /> */}
      {round === 'ChoiceTurn' && (
        <TurnCard handleChoiceTurnCard={handleChoiceTurnCard} />
      )}

      {/* <S.BottomRow>
        <S.ManyChips src={images.gsb.allChipsBet} />
        <S.AllBetChips>10</S.AllBetChips>
      </S.BottomRow> */}
    </S.GameRoomContainer>
  )
}

export default withAuth(GameRoom)
