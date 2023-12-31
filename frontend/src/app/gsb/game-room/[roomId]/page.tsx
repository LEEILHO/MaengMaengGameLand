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
import { useRecoilState, useRecoilValue, useResetRecoilState } from 'recoil'
import {
  AllBetChipsState,
  CurrentPlayerState,
  DisplayMessageState,
  GameOverState,
  MyBetChipsState,
  MyState,
  OpponentBetChipsState,
  OpponentState,
  ResultState,
  RoundState,
  TimerState,
  TurnCardState,
} from '@atom/gsbAtom'
import { userState } from '@atom/userAtom'
import GameOver from '@components/gsb/client/GameOver'
import useDidMountEffect from '@hooks/useDidMoundEffect'
import { getMs } from '@utils/gameRoom/gsbUtil'
import useSound from '@hooks/useSound'

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
  const { playButtonSound } = useSound()
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
  const gameOver = useRecoilValue(GameOverState)

  const [isGameEnd, setIsGameEnd] = useState(false)

  // 리셋
  const resetTurnCard = useResetRecoilState(TurnCardState)
  const resetRound = useResetRecoilState(RoundState)
  const resetTimer = useResetRecoilState(TimerState)
  const resetDisplayMessage = useResetRecoilState(DisplayMessageState)
  const resetCurrentPlayer = useResetRecoilState(CurrentPlayerState)
  const resetAllBetChips = useResetRecoilState(AllBetChipsState)
  const resetMy = useResetRecoilState(MyState)
  const resetOpponent = useResetRecoilState(OpponentState)
  const resetMyBet = useResetRecoilState(MyBetChipsState)
  const resetOpponentBet = useResetRecoilState(OpponentBetChipsState)
  const resetResult = useResetRecoilState(ResultState)
  const resetGameOver = useResetRecoilState(GameOverState)

  useEffect(() => {
    connectSocket(connectGsb, disconnectGsb)
    return () => {
      disconnectSocket()
    }
  }, [])

  useDidMountEffect(() => {
    let timeId: NodeJS.Timeout
    console.log(gameOver)
    if (gameOver && round) {
      timeId = setTimeout(() => {
        // 게임 종료
        setRound('GameOver')
        setDisplayMessage('게임이 종료되었습니다')
        setIsGameEnd(true)
      }, getMs(round))
    }

    return () => {
      clearTimeout(timeId)
    }
  }, [gameOver])

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
              playButtonSound()
              resetTurnCard()
              resetAllBetChips()
              resetCurrentPlayer()
              resetDisplayMessage()
              resetGameOver()
              resetMyBet()
              resetMy()
              resetOpponent()
              resetOpponentBet()
              resetResult()
              resetRound()
              resetTimer()
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
