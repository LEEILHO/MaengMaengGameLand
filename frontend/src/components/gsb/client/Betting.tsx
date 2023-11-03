'use client'

import CButton from '@components/common/clients/CButton'
import { colors } from '@constants/colors'
import { images } from '@constants/images'
import * as S from '@styles/gsb/Betting.styled'
import { useState } from 'react'

type Props = {
  minBet: number
  chipsPlayerHas: number
}

// 입력란과 베팅, 포기 버튼이 있는 바텀 컴포넌트
const Betting = ({ minBet, chipsPlayerHas }: Props) => {
  const [chipCount, setChipCount] = useState(minBet)
  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setChipCount(Number(e.target.value))
  }

  const onClickBetButton = () => {
    if (minBet > chipCount) {
      console.log('최소 베팅 수를 맞춰야 함')
    } else if (chipsPlayerHas < chipCount) {
      console.log('베팅할 칩이 모자름')
    }
    // 하프, 콜, 올인 등 체크해야 할 경우가 많음
  }

  const onClickGiveUpButton = () => {
    console.log('포기')
  }

  return (
    <S.BettingContainer>
      <CButton
        color="white"
        backgroundColor={colors.button.darkPurple}
        text="포기"
        fontSize={20}
        width={118}
        radius={50}
        onClick={onClickGiveUpButton}
      />
      <S.BettingInputContainer>
        <img src={images.gsb.chip} alt="chip" />
        <S.BettingInput type="text" value={chipCount} onChange={onChange} />
      </S.BettingInputContainer>
      <CButton
        color="white"
        backgroundColor={colors.button.purple}
        text="베팅"
        fontSize={20}
        width={118}
        radius={50}
        onClick={onClickBetButton}
      />
    </S.BettingContainer>
  )
}

export default Betting
