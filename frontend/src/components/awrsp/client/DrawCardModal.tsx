'use client'

import { DrawCardState } from '@atom/awrspAtom'
import CButton from '@components/common/clients/CButton'
import { colors } from '@constants/colors'
import { images } from '@constants/images'
import useDidMountEffect from '@hooks/useDidMoundEffect'
import useSound from '@hooks/useSound'
import * as S from '@styles/awrsp/DrawCardModal.styled'
import { RspType, StepType } from '@type/awrsp/awrsp.type'
import { useCallback } from 'react'
import { useRecoilState, useRecoilValue } from 'recoil'

type Props = {
  closeModal: () => void
  step: StepType
}

const DrawCardModal = ({ closeModal, step }: Props) => {
  const { playFlipCardSound, playButtonSound } = useSound()
  const [drawCard, setDrawCard] = useRecoilState(DrawCardState)

  const onClickDrawCard = useCallback((cardType: RspType) => {
    console.log(cardType)
    playFlipCardSound()
    setDrawCard({ ...drawCard, drawCard: cardType })
    closeModal()
  }, [])

  const onClickSkip = useCallback(() => {
    playButtonSound()
    setDrawCard({ drawCard: null, isSetting: true })
    closeModal()
  }, [])

  useDidMountEffect(() => {
    if (step === 'CARD_SUBMIT') {
      setDrawCard({ drawCard: null, isSetting: true })
      closeModal()
    }
  }, [step])

  return (
    <S.DrawCardModalContainer>
      <S.TopRow>
        <S.Title>비김 알림 카드</S.Title>
      </S.TopRow>
      <S.CardListContainer>
        <S.DrawCard onClick={() => onClickDrawCard('DRAW_SCISSOR')}>
          <img src={images.awrsp.scissorsDrawCard} alt="가위 비김" />
        </S.DrawCard>
        <S.DrawCard onClick={() => onClickDrawCard('DRAW_ROCK')}>
          <img src={images.awrsp.rockDrawCard} alt="바위 비김" />
        </S.DrawCard>
        <S.DrawCard onClick={() => onClickDrawCard('DRAW_PAPER')}>
          <img src={images.awrsp.paperDrawCard} alt="보 비김" />
        </S.DrawCard>
      </S.CardListContainer>
      <S.BottomRow>
        <S.Description>사용할 카드를 선택하세요</S.Description>
        <S.SkipButton onClick={onClickSkip}>skip</S.SkipButton>
      </S.BottomRow>
    </S.DrawCardModalContainer>
  )
}

export default DrawCardModal
