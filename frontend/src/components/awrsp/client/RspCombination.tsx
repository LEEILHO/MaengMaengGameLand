'use client'

import { DrawCardState, RspCardListState } from '@atom/awrspAtom'
import CButton from '@components/common/clients/CButton'
import { colors } from '@constants/colors'
import useSocketAWRSP from '@hooks/useSocketAWRSP'
import * as S from '@styles/awrsp/RspCombination.styled'
import {
  CardListType,
  CardStatus,
  CardType,
  RspType,
} from '@type/awrsp/awrsp.type'
import { getRspImageUrl } from '@utils/awrsp/awrspUtil'
import { useEffect, useState } from 'react'
import {
  DragDropContext,
  Draggable,
  DropResult,
  Droppable,
} from 'react-beautiful-dnd'
import { useRecoilState, useSetRecoilState } from 'recoil'

type Props = {
  handleCardSubmit: (combCard: RspType[]) => void
}

const RspCombination = ({ handleCardSubmit }: Props) => {
  const setRspCardList = useSetRecoilState(RspCardListState)
  const [enabled, setEnabled] = useState(false)
  const [drawCard, setDrawCard] = useRecoilState(DrawCardState)
  const [isSubmit, setIsSubmit] = useState(false)

  const rock: CardType[] = [...Array(3)].map((_, i) => ({
    id: `Rock${i}`,
    status: 'out',
    rsp: 'ROCK',
  }))
  const scissors: CardType[] = [...Array(3)].map((_, i) => ({
    id: `Scissors${i}`,
    status: 'out',
    rsp: 'SCISSOR',
  }))
  const paper: CardType[] = [...Array(3)].map((_, i) => ({
    id: `Paper${i}`,
    status: 'out',
    rsp: 'PAPER',
  }))

  const [cardList, setCardList] = useState<CardListType>({
    out: [...rock, ...scissors, ...paper],

    in: [],
  })

  // 드래그가 끝났을 때 실행되는 함수
  const onDragEnd = ({ source, destination }: DropResult) => {
    if (!destination) return

    console.log('>>> source ', source)
    console.log('>>> destination ', destination)

    const sourceKey = source.droppableId as CardStatus
    const destinationKey = destination.droppableId as CardStatus

    if (
      sourceKey === 'out' &&
      sourceKey !== destinationKey &&
      cardList['in'].length === 7
    )
      return
    const _cardList = JSON.parse(JSON.stringify(cardList)) as typeof cardList
    const [targetCard] = _cardList[sourceKey].splice(source.index, 1)
    _cardList[destinationKey].splice(destination.index, 0, targetCard)

    setCardList(_cardList)
  }

  const onClickSubmitButton = () => {
    if (cardList['in'].length < 7) {
      console.log('모자라...')
      return
    }
    const RspCombination = cardList['in'].map((card: CardType) => {
      return card.rsp
    })

    setIsSubmit(true)
    setRspCardList(RspCombination)
    handleCardSubmit(RspCombination)
  }

  useEffect(() => {
    if (drawCard.drawCard) {
      const draw: CardType = {
        id: drawCard.drawCard,
        status: 'out',
        rsp: drawCard.drawCard,
      }
      const _cardList = cardList

      if (drawCard.drawCard === 'DRAW_PAPER') {
        _cardList['out'].splice(6, 1)
        _cardList['out'].splice(6, 0, draw)
      } else if (drawCard.drawCard === 'DRAW_SCISSOR') {
        _cardList['out'].splice(3, 1)
        _cardList['out'].splice(3, 0, draw)
      } else if (drawCard.drawCard === 'DRAW_ROCK') {
        _cardList['out'].splice(0, 1)
        _cardList['out'].splice(0, 0, draw)
      }

      setCardList(_cardList)
      setDrawCard({ ...drawCard, isSetting: true })
    }
  }, [drawCard.drawCard])

  useEffect(() => {
    const animation = requestAnimationFrame(() => setEnabled(true))

    return () => {
      cancelAnimationFrame(animation)
      setEnabled(false)
    }
  }, [])

  if (!enabled || !drawCard.isSetting) return null

  return (
    <>
      <S.Container>
        <DragDropContext onDragEnd={onDragEnd}>
          <S.DragDropContextDiv>
            <Droppable key={'in'} droppableId="in" direction="horizontal">
              {(provided) => (
                <S.InDropArea
                  ref={provided.innerRef}
                  {...provided.droppableProps}
                >
                  {cardList['in' as CardStatus].map((card, index) => (
                    <Draggable
                      key={card.id}
                      draggableId={card.id}
                      index={index}
                    >
                      {(provided, snapshot) => (
                        <S.DragCard
                          ref={provided.innerRef}
                          {...provided.dragHandleProps}
                          {...provided.draggableProps}
                          isDragging={snapshot.isDragging}
                        >
                          <img src={getRspImageUrl(card.rsp)} alt={card.rsp} />
                        </S.DragCard>
                      )}
                    </Draggable>
                  ))}
                  {provided.placeholder}
                </S.InDropArea>
              )}
            </Droppable>
            <Droppable key={'out'} droppableId="out" direction="horizontal">
              {(provided) => (
                <S.OutDropArea
                  ref={provided.innerRef}
                  {...provided.droppableProps}
                >
                  {cardList['out' as CardStatus].map((card, index) => (
                    <Draggable
                      key={card.id}
                      draggableId={card.id}
                      index={index}
                    >
                      {(provided, snapshot) => (
                        <S.DragCard
                          ref={provided.innerRef}
                          {...provided.dragHandleProps}
                          {...provided.draggableProps}
                          isDragging={snapshot.isDragging}
                        >
                          <img src={getRspImageUrl(card.rsp)} alt={card.rsp} />
                        </S.DragCard>
                      )}
                    </Draggable>
                  ))}
                  {provided.placeholder}
                </S.OutDropArea>
              )}
            </Droppable>
          </S.DragDropContextDiv>
        </DragDropContext>
      </S.Container>
      <S.BottomButton>
        {isSubmit ? (
          <CButton
            backgroundColor={colors.greyScale.grey300}
            width={100}
            height={36}
            color="white"
            fontSize={16}
            text="제출 완료"
            radius={64}
          />
        ) : (
          <CButton
            backgroundColor={colors.button.purple}
            width={100}
            height={36}
            color="white"
            fontSize={16}
            text="제출"
            radius={64}
            onClick={onClickSubmitButton}
          />
        )}
      </S.BottomButton>
    </>
  )
}

export default RspCombination
