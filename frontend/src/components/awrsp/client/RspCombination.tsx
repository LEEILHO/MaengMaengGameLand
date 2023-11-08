'use client'

import { DrawCardState } from '@atom/awrspAtom'
import CButton from '@components/common/clients/CButton'
import { colors } from '@constants/colors'
import * as S from '@styles/awrsp/RspCombination.styled'
import { CardListType, CardStatus, CardType } from '@type/awrsp/awrsp.type'
import { getRspImageUrl } from '@utils/awrsp/awrspUtil'
import { useEffect, useState } from 'react'
import {
  DragDropContext,
  Draggable,
  DropResult,
  Droppable,
} from 'react-beautiful-dnd'
import { useRecoilValue } from 'recoil'

const RspCombination = () => {
  const [enabled, setEnabled] = useState(false)
  const drawCard = useRecoilValue(DrawCardState)
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

    console.log(RspCombination)
  }

  useEffect(() => {
    const animation = requestAnimationFrame(() => setEnabled(true))

    return () => {
      cancelAnimationFrame(animation)
      setEnabled(false)
    }
  }, [])

  if (!enabled) return null

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
                      {(provided) => (
                        <S.DragCard
                          ref={provided.innerRef}
                          {...provided.dragHandleProps}
                          {...provided.draggableProps}
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
                      {(provided) => (
                        <S.DragCard
                          ref={provided.innerRef}
                          {...provided.dragHandleProps}
                          {...provided.draggableProps}
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
        <CButton
          backgroundColor={colors.button.purple}
          width={76}
          height={32}
          color="white"
          fontSize={16}
          text="제출"
          radius={64}
          onClick={onClickSubmitButton}
        />
      </S.BottomButton>
    </>
  )
}

export default RspCombination
