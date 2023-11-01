'use client'

import React, { useState } from 'react'
import * as S from '@styles/gsb/OrderingCard.styled'
import { TurnListType } from '@type/gsb/game.type'

// 선후공 카드가 나오는 페이지
const TurnCard = () => {
  const [cardChoice, setCardChoice] = useState(false)
  const [orderList, setOrderList] = useState<TurnListType[]>([
    {
      first: true,
      selected: false,
    },
    {
      first: false,
      selected: false,
    },
  ])

  const onChoiceCard = () => {}

  return (
    <S.OrderingCardContainer>
      <S.DisplayBoard>카드를 뒤집어 선공을 정해주세요.</S.DisplayBoard>
      <S.CardList></S.CardList>
    </S.OrderingCardContainer>
  )
}

export default TurnCard
