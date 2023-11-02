'use client'

import * as S from '@styles/gsb/DragAndDrop.styled'
import { useEffect, useState } from 'react'
import { images } from '@constants/images'
import { StarListType, StarStatus } from '@type/gsb/game.type'
import { Draggable, Droppable } from 'react-beautiful-dnd'

type Props = {
  stars: StarListType
  starClass: string
}

const DragAndDrop = ({ stars, starClass }: Props) => {
  return (
    <S.DragAndDropContainer>
      {Object.keys(stars).map((key) => (
        <Droppable key={key} droppableId={key} direction="horizontal">
          {(provided) => (
            <S.DropArea isIn={key === 'in' ? true : false}>
              {key === 'in' && <span>{starClass}</span>}
              <div
                className="area"
                ref={provided.innerRef}
                {...provided.droppableProps}
              >
                {stars[key as StarStatus].map((star, index) => (
                  <Draggable key={star.id} draggableId={star.id} index={index}>
                    {(provided) => (
                      <S.Star
                        ref={provided.innerRef}
                        {...provided.dragHandleProps}
                        {...provided.draggableProps}
                        src={star.src}
                        index={index}
                      />
                    )}
                  </Draggable>
                ))}
              </div>
              {provided.placeholder}
            </S.DropArea>
          )}
        </Droppable>
      ))}
    </S.DragAndDropContainer>
  )
}

export default DragAndDrop
