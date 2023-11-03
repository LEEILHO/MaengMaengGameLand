'use client'

import { images } from '@constants/images'
import * as S from '@styles/gsb/Betting.styled'

type Props = {
  betChips: number[]
}
// Content에 베팅현황을 보여주는 컴포넌트
const BettingStatus = ({ betChips }: Props) => {
  return (
    <S.BettingStatusContainer>
      {betChips.map((chips, index) => (
        <S.Chips key={index}>
          <p>{chips}</p>
          <img src={images.gsb.chips} alt="베팅된 칩" />
        </S.Chips>
      ))}
    </S.BettingStatusContainer>
  )
}

export default BettingStatus
