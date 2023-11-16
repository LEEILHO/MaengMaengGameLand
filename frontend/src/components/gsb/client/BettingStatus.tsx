'use client'

import { images } from '@constants/images'
import * as S from '@styles/gsb/Betting.styled'

type Props = {
  myBet: number
  opponentBet: number
}
// Content에 베팅현황을 보여주는 컴포넌트
const BettingStatus = ({ myBet, opponentBet }: Props) => {
  return (
    <S.BettingStatusContainer>
      {myBet !== 0 && (
        <S.Chips>
          <p>{myBet}</p>
          <img src={images.gsb.chips} alt="베팅된 칩" />
        </S.Chips>
      )}
      {opponentBet !== 0 && (
        <S.Chips>
          <p>{opponentBet}</p>
          <img src={images.gsb.chips} alt="베팅된 칩" />
        </S.Chips>
      )}
    </S.BettingStatusContainer>
  )
}

export default BettingStatus
