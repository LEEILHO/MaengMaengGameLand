'use client'

import { DrawCardState, StepState } from '@atom/awrspAtom'
import { images } from '@constants/images'
import useDidMountEffect from '@hooks/useDidMoundEffect'
import * as S from '@styles/awrsp/DrawCardModal.styled'
import { RspType } from '@type/awrsp/awrsp.type'
import { useCallback } from 'react'
import { useRecoilState, useRecoilValue } from 'recoil'

type Props = {
  closeModal: () => void
}

const DrawCardModal = ({ closeModal }: Props) => {
  const step = useRecoilValue(StepState)
  const [drawCard, setDrawCard] = useRecoilState(DrawCardState)

  const onClickDrawCard = useCallback((cardType: RspType) => {
    console.log(cardType)
    setDrawCard({ ...drawCard, drawCard: cardType })
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
      <S.Title>비김 알림 카드</S.Title>
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
      <S.Description>사용할 카드를 선택하세요</S.Description>
    </S.DrawCardModalContainer>
  )
}

export default DrawCardModal
