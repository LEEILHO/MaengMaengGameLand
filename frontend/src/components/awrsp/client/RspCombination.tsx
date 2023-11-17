'use client'

import { DrawCardState, RspCardListState } from '@atom/awrspAtom'
import CButton from '@components/common/clients/CButton'
import { colors } from '@constants/colors'
import useSound from '@hooks/useSound'
import * as S from '@styles/awrsp/RspCombination.styled'
import { CardStatus, CardType, RspType } from '@type/awrsp/awrsp.type'
import { getRspImageUrl } from '@utils/awrsp/awrspUtil'
import { useEffect, useState, useCallback } from 'react'
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
  const { playButtonSound, playDropCardSound, playFlipCardSound } = useSound()
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

  const [outCardList, setOutCardList] = useState<CardType[]>([
    ...rock,
    ...scissors,
    ...paper,
  ])

  const [inCardList, setInCardList] = useState<CardType[]>(
    [...Array(7)].map((_, index) => ({
      id: `Empty${index}`,
      status: 'in',
      rsp: 'EMPTY',
    })),
  )

  // 드래그가 끝났을 때 실행되는 함수
  const onDragEnd = ({ source, destination }: DropResult) => {
    playDropCardSound()
    console.log('>>> source ', source)
    console.log('>>> destination ', destination)
    if (!destination) return

    const sourceKey = source.droppableId as CardStatus
    const destinationKey = destination.droppableId as CardStatus

    if (sourceKey === 'out' && destinationKey.substring(0, 2) === 'in') {
      const destIndex = Number(destinationKey.substring(2, 3))
      const _outCardList = [...outCardList]
      const _inCardList = [...inCardList]
      const [targetCard] = _outCardList.splice(source.index, 1)

      console.log(_inCardList[destIndex], targetCard)

      if (inCardList[destIndex].rsp !== 'EMPTY') {
        const removeCard = _inCardList[destIndex]
        _inCardList[destIndex] = targetCard
        _outCardList.splice(source.index, 0, removeCard)
      } else {
        _inCardList[destIndex] = targetCard
      }

      setOutCardList(_outCardList)
      setInCardList(_inCardList)
    } else if (
      sourceKey.substring(0, 2) === 'in' &&
      destinationKey.substring(0, 2) === 'in'
    ) {
      const srcIndex = Number(sourceKey.substring(2, 3))
      const destIndex = Number(destinationKey.substring(2, 3))
      const _inCardList = [...inCardList]
      const temp = _inCardList[srcIndex]

      _inCardList[srcIndex] = _inCardList[destIndex]
      _inCardList[destIndex] = temp

      setInCardList(_inCardList)
    } else if (sourceKey.substring(0, 2) === 'in' && destinationKey === 'out') {
      const _inCardList = [...inCardList]
      const targetCard = _inCardList[source.index]
      const _outCardList = [...outCardList]
      _outCardList.splice(destination.index, 0, targetCard)
      _inCardList[source.index] = {
        id: `Empty${source.index}`,
        status: 'in',
        rsp: 'EMPTY',
      }

      setOutCardList(_outCardList)
      setInCardList(_inCardList)
    }
  }

  const onDragStart = useCallback(() => {
    playFlipCardSound()
  }, [])

  const onClickSubmitButton = () => {
    playButtonSound()
    if (inCardList.filter((card) => card.rsp !== 'EMPTY').length < 7) {
      console.log('모자라...')
      return
    }
    const RspCombination = inCardList.map((card: CardType) => {
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
      const _cardList = outCardList

      setOutCardList([..._cardList, draw])
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
        <DragDropContext onDragEnd={onDragEnd} onDragStart={onDragStart}>
          <S.DragDropContextDiv>
            <S.InDropArea>
              {inCardList.map((card, index) => (
                <Droppable key={`in${index}`} droppableId={`in${index}`}>
                  {(provided) => (
                    <S.CardSlot
                      ref={provided.innerRef}
                      {...provided.droppableProps}
                    >
                      <Draggable
                        key={card.id}
                        draggableId={`in${index}`}
                        index={index}
                        isDragDisabled={isSubmit}
                      >
                        {(provided, snapshot) => (
                          <S.DragCard
                            ref={provided.innerRef}
                            {...provided.dragHandleProps}
                            {...provided.draggableProps}
                            isDragging={snapshot.isDragging}
                          >
                            {card.rsp !== 'EMPTY' ? (
                              <img
                                src={getRspImageUrl(card.rsp)}
                                alt={card.rsp}
                              />
                            ) : (
                              ' '
                            )}
                          </S.DragCard>
                        )}
                      </Draggable>
                      {provided.placeholder}
                    </S.CardSlot>
                  )}
                </Droppable>
              ))}
            </S.InDropArea>

            <Droppable key={'out'} droppableId="out" direction="horizontal">
              {(provided) => (
                <S.OutDropArea
                  ref={provided.innerRef}
                  {...provided.droppableProps}
                >
                  {outCardList.map((card, index) => (
                    <Draggable
                      key={card.id}
                      draggableId={card.id}
                      index={index}
                      isDragDisabled={isSubmit}
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
