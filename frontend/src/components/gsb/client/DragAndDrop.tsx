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
      <Droppable key={'in'} droppableId={'in'} direction="horizontal">
        {(provided) => (
          <S.InDropArea>
            <span>{starClass}</span>
            <div
              className="area"
              ref={provided.innerRef}
              {...provided.droppableProps}
            >
              {stars['in' as StarStatus].map((star, index) => (
                <Draggable key={star.id} draggableId={star.id} index={index}>
                  {(provided) => (
                    <S.Star
                      ref={provided.innerRef}
                      {...provided.dragHandleProps}
                      {...provided.draggableProps}
                      src={star.src}
                      columns={5}
                      index={index}
                    />
                  )}
                </Draggable>
              ))}
            </div>
            {provided.placeholder}
          </S.InDropArea>
        )}
      </Droppable>
      <Droppable key={'out'} droppableId={'out'} direction="horizontal">
        {(provided) => (
          <S.OutDropArea>
            <div
              className="area"
              ref={provided.innerRef}
              {...provided.droppableProps}
            >
              {stars['out' as StarStatus].map((star, index) => (
                <Draggable key={star.id} draggableId={star.id} index={index}>
                  {(provided) => (
                    <S.Star
                      ref={provided.innerRef}
                      {...provided.dragHandleProps}
                      {...provided.draggableProps}
                      src={star.src}
                      columns={10}
                      index={index}
                    />
                  )}
                </Draggable>
              ))}
            </div>
            {provided.placeholder}
          </S.OutDropArea>
        )}
      </Droppable>
    </S.DragAndDropContainer>
  )
}

export default DragAndDrop
