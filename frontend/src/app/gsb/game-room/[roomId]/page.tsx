'use client'

import withAuth from '@components/hoc/client/PrivateRoute'
import { useEffect, useState } from 'react'

import * as S from '@styles/gsb/GameRoom.styled'
import TurnCard from '@components/gsb/client/TurnCard'
import PlayerCard from '@components/gsb/client/PlayerCard'
import { images } from '@constants/images'
import CombinationGsb from '@components/gsb/client/CombinationGsb'
import Betting from '@components/gsb/client/Betting'
import BettingStatus from '@components/gsb/client/BettingStatus'
import RoundResult from '@components/gsb/client/RoundResult'
import BarTimer from '@components/common/clients/BarTimer'
import useSocketGsb from '@hooks/useSocketGsb'
import { useRouter } from 'next/navigation'
import { useRecoilState, useRecoilValue } from 'recoil'
import {
  AllBetChipsState,
  CurrentPlayerState,
  DisplayMessageState,
  GameOverState,
  MyBetChipsState,
  MyState,
  OpponentBetChipsState,
  OpponentState,
  RoundState,
  TimerState,
} from '@atom/gsbAtom'
import { userState } from '@atom/userAtom'
import GameOver from '@components/gsb/client/GameOver'

const GameRoom = () => {
  const router = useRouter()
  const {
    connectSocket,
    disconnectSocket,
    connectGsb,
    disconnectGsb,
    handleChoiceTurnCard,
    handleGSBComb,
    handleBetting,
  } = useSocketGsb()
  // const gameCode = usePathname().split('/')[3]

  // 전광판 하나로 해서 상황에 따라 메세지만 바꾸기
  const [displayMessage, setDisplayMessage] =
    useRecoilState(DisplayMessageState)
  const [round, setRound] = useRecoilState(RoundState)
  const time = useRecoilValue(TimerState)
  const my = useRecoilValue(MyState)
  const opponent = useRecoilValue(OpponentState)
  const myBetChips = useRecoilValue(MyBetChipsState)
  const opponentBetChips = useRecoilValue(OpponentBetChipsState)
  const AllBetChips = useRecoilValue(AllBetChipsState)

  const [isGameEnd, setIsGameEnd] = useState(false)

  useEffect(() => {
    connectSocket(connectGsb, disconnectGsb)
    return () => {
      disconnectSocket()
    }
  }, [])

  useEffect(() => {
    let timeId: NodeJS.Timeout
    if (round === 'GameOver') {
      timeId = setTimeout(() => {
        setIsGameEnd(true)
      }, 5000)
    }

    return () => {
      clearTimeout(timeId)
    }
  }, [round])

  return (
    <S.GameRoomContainer>
      <S.TopRow>
        <S.DisplayBoard>{displayMessage}</S.DisplayBoard>
      </S.TopRow>
      {isGameEnd ? (
        <>
          <GameOver />
          <S.BackButton
            src={images.gameRoom.jwac.backWhite}
            alt="로비로 나가기"
            onClick={() => {
              router.replace('/gsb/lobby')
            }}
          />
        </>
      ) : (
        <>
          <S.CenterRow>
            <PlayerCard player={my} />
            <S.Content>
              {round === 'Combination' && (
                <CombinationGsb handleGSBComb={handleGSBComb} />
              )}
              {(round === 'Betting' ||
                round === 'BetWaiting' ||
                round === 'Combination' ||
                round === 'CombWaiting') && (
                <BettingStatus
                  myBet={myBetChips}
                  opponentBet={opponentBetChips}
                />
              )}
              {(round === 'Result' ||
                round === 'DrawResult' ||
                round === 'GiveUpResult') && <RoundResult />}
            </S.Content>
            <PlayerCard player={opponent} />
          </S.CenterRow>
          {round === 'Betting' && <Betting handleBetting={handleBetting} />}
          {round === 'ChoiceTurn' && (
            <TurnCard handleChoiceTurnCard={handleChoiceTurnCard} />
          )}

          {round === 'Result' && (
            <S.BottomRow>
              <S.ManyChips src={images.gsb.allChipsBet} />
              <S.AllBetChips>{AllBetChips}</S.AllBetChips>
            </S.BottomRow>
          )}
        </>
      )}
    </S.GameRoomContainer>
  )
}

export default withAuth(GameRoom)
