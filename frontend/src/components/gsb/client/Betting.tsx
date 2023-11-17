'use client'

import {
  MyBetChipsState,
  MyState,
  OpponentBetChipsState,
  OpponentState,
} from '@atom/gsbAtom'
import CButton from '@components/common/clients/CButton'
import { colors } from '@constants/colors'
import { images } from '@constants/images'
import useModal from '@hooks/useModal'
import * as S from '@styles/gsb/Betting.styled'
import { useState } from 'react'
import { useRecoilValue } from 'recoil'
import AlertModal from './AlertModal'
import useSound from '@hooks/useSound'

type Props = {
  handleBetting: (giveUp: boolean, bettingChips: number) => void
}

// 입력란과 베팅, 포기 버튼이 있는 바텀 컴포넌트
const Betting = ({ handleBetting }: Props) => {
  const { playButtonSound, playBettingSound } = useSound()
  const { Modal, isOpen, closeModal, openModal } = useModal()
  const [text, setText] = useState<string>('')
  const [chipCount, setChipCount] = useState<number>(0)
  const myBetChips = useRecoilValue(MyBetChipsState)
  const opponentBetChips = useRecoilValue(OpponentBetChipsState)
  const opponent = useRecoilValue(OpponentState)

  const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setChipCount(
      Number(e.target.value.replace(/[^0-9]/g, '').replace(/(^0+)/, '')),
    )
  }

  const onClickBetButton = () => {
    playButtonSound()
    console.log('my: ', myBetChips, ' oppoonent: ', opponentBetChips)

    const minBet = opponentBetChips - myBetChips

    console.log('minBet:', minBet, ' chipCount:', chipCount)

    if (chipCount < minBet) {
      setText('최소 배팅 개수를 맞춰주세요.')
      openModal()
    } else if (
      opponent &&
      opponent?.currentChips + (opponentBetChips - myBetChips) < chipCount
    ) {
      setText('상대방의 보유 칩보다 많이 베팅할 수 없습니다.')
      openModal()
    } else if (chipCount > 0 && chipCount >= minBet) {
      playBettingSound()
      handleBetting(false, chipCount)
    }
  }

  const onClickGiveUpButton = () => {
    console.log('포기')
    playButtonSound()
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

      <Modal isOpen={isOpen} closeModal={closeModal}>
        <AlertModal text={text} closeModal={closeModal} />
      </Modal>
    </S.BettingContainer>
  )
}

export default Betting
