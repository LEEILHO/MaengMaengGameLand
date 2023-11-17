'use client'

import { useEffect, useState } from 'react'
import * as S from '@styles/gsb/GameOver.styled'
import PlayerReultCard from './PlayerReultCard'
import { useRecoilValue } from 'recoil'
import { GameOverState, MyState, OpponentState } from '@atom/gsbAtom'

const GameOver = () => {
  const gameResult = useRecoilValue(GameOverState)
  const my = useRecoilValue(MyState)
  const opponent = useRecoilValue(OpponentState)
  const [isWin, setIsWin] = useState(false)

  useEffect(() => {
    if (gameResult) {
      if (gameResult.winner === my?.nickname) setIsWin(true)
    }
  }, [])

  if (!gameResult || !my || !opponent) return null

  return (
    <S.GameOverContainer>
      <PlayerReultCard
        nickname={isWin ? gameResult.winner : gameResult.loser}
        profileUrl={my.profileUrl}
        chips={isWin ? gameResult.winnerChips : gameResult.loserChips}
        result={gameResult.draw ? 'draw' : isWin ? 'win' : 'lose'}
      />
      <PlayerReultCard
        nickname={isWin ? gameResult.loser : gameResult.winner}
        profileUrl={opponent.profileUrl}
        chips={isWin ? gameResult.loserChips : gameResult.winnerChips}
        result={gameResult.draw ? 'draw' : isWin ? 'lose' : 'win'}
      />
    </S.GameOverContainer>
  )
}

export default GameOver
