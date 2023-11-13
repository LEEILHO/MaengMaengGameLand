'use client'

import React, { useState } from 'react'
import * as S from '@styles/gsb/TurnCard.styled'
import { images } from '@constants/images'
import { TurnCardState } from '@atom/gsbAtom'
import { useRecoilValue } from 'recoil'

type Props = {
  handleChoiceTurnCard: (index: number) => void
}

// 선후공 카드가 나오는 페이지
const TurnCard = ({ handleChoiceTurnCard }: Props) => {
  const [cardChoice, setCardChoice] = useState(false)
  const turnList = useRecoilValue(TurnCardState)

  const onChoiceCard = (index: number) => {
    console.log(turnList)
    if (!turnList || cardChoice || turnList[index].selected) return
    setCardChoice(true)
    handleChoiceTurnCard(index)
  }

  if (!turnList) return

  return (
    <S.OrderingCardContainer>
      <S.CardList>
        {turnList.map((card, index) => (
          <S.CardContainer
            key={index}
            initial={false}
            animate={{ rotateY: card.selected ? 180 : 360 }}
            transition={{ duration: 0.4, animationDirection: 'normal' }}
            onClick={() => {
              onChoiceCard(index)
            }}
          >
            <S.CardFront
              src={
                card.seq === 0
                  ? images.gsb.cardFrontSun
                  : images.gsb.cardFrontHu
              }
            />
            <S.CardBack src={images.gsb.cardBack} />
          </S.CardContainer>
        ))}
      </S.CardList>
    </S.OrderingCardContainer>
  )
}

export default TurnCard
