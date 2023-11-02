'use client'

import React, { useState } from 'react'
import * as S from '@styles/gsb/TurnCard.styled'
import { TurnListType } from '@type/gsb/game.type'
import { images } from '@constants/images'

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

  const onChoiceCard = (index: number) => {
    if (cardChoice || orderList[index].selected) return
    setCardChoice(true)
    let newOrderList = [...orderList]
    orderList[index].selected = true
    setOrderList(newOrderList)
  }

  return (
    <S.OrderingCardContainer>
      <S.CardList>
        {orderList.map((order, index) => (
          <S.CardContainer
            key={index}
            initial={false}
            animate={{ rotateY: order.selected ? 180 : 360 }}
            transition={{ duration: 0.4, animationDirection: 'normal' }}
            onClick={() => {
              onChoiceCard(index)
            }}
          >
            <S.CardFront
              src={
                order.first ? images.gsb.cardFrontSun : images.gsb.cardFrontHu
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
