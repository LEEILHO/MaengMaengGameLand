'use client'

import { MyState, OpponentState } from '@atom/gsbAtom'
import CButton from '@components/common/clients/CButton'
import { colors } from '@constants/colors'
import { images } from '@constants/images'
import * as S from '@styles/gsb/Betting.styled'
import { useState } from 'react'
import { useRecoilValue } from 'recoil'

type Props = {
  handleBetting: (giveUp: boolean, bettingChips: number) => void
}

// 입력란과 베팅, 포기 버튼이 있는 바텀 컴포넌트
const Betting = ({ handleBetting }: Props) => {
  const [chipCount, setChipCount] = useState<number>(0)
  const my = useRecoilValue(MyState)
  const opponent = useRecoilValue(OpponentState)
  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setChipCount(Number(e.target.value))
  }

  const onClickBetButton = () => {
    console.log('my: ', my, ' oppoonent: ', opponent)

    if (!my || !opponent) return null
    const minBet = opponent?.currentBetChips - my?.currentBetChips

    console.log('minBet:', minBet, ' chipCount:', chipCount)

    if (chipCount > 0 && chipCount >= minBet) {
      handleBetting(false, chipCount)
    } else if (chipCount < minBet) {
      console.log('최소 베팅 금액을 맞춰주세요')
    }
  }

  const onClickGiveUpButton = () => {
    console.log('포기')
    handleBetting(true, 0)
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
