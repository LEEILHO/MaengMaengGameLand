'use client'

import * as S from '@styles/gsb/CombinationGsb.styled'
import { images } from '@constants/images'
import { StarListType, StarStatus } from '@type/gsb/gsb.type'
import { useEffect, useState } from 'react'
import { DropResult } from 'react-beautiful-dnd'
import DragAndDrop from './DragAndDrop'
import CButton from '@components/common/clients/CButton'
import { colors } from '@constants/colors'

const CombinationGsb = () => {
  const [enabled, setEnabled] = useState(false)
  const [goldStars, setGoldStars] = useState<StarListType>({
    in: [],
    out: [...Array(3)].map((_, i) => ({
      id: `gold${i}`,
      src: images.gsb.goldStar,
      status: 'out',
    })),
  })

  const [silverStars, setSilverStars] = useState<StarListType>({
    in: [],
    out: [...Array(10)].map((_, i) => ({
      id: `silver${i}`,
      src: images.gsb.silverStar,
      status: 'out',
    })),
  })

  const [bronzeStars, setBronzeStars] = useState<StarListType>({
    in: [],
    out: [...Array(20)].map((_, i) => ({
      id: `bronze${i}`,
      src: images.gsb.bronzeStar,
      status: 'out',
    })),
  })

  const onDragEndGold = ({ source, destination }: DropResult) => {
    if (!destination) return

    const sourceKey = source.droppableId as StarStatus
    const destinationKey = destination.droppableId as StarStatus

    if (sourceKey === destinationKey) return

    const _stars = JSON.parse(JSON.stringify(goldStars)) as typeof goldStars
    const [targetStar] = _stars[sourceKey].splice(source.index, 1)
    _stars[destinationKey].splice(destination.index, 0, targetStar)
    setGoldStars(_stars)
  }

  const onDragEndSilver = ({ source, destination }: DropResult) => {
    if (!destination) return

    const sourceKey = source.droppableId as StarStatus
    const destinationKey = destination.droppableId as StarStatus

    if (sourceKey === destinationKey) return

    const _stars = JSON.parse(JSON.stringify(silverStars)) as typeof silverStars
    const [targetStar] = _stars[sourceKey].splice(source.index, 1)
    _stars[destinationKey].splice(destination.index, 0, targetStar)
    setSilverStars(_stars)
  }

  const onDragEndBronze = ({ source, destination }: DropResult) => {
    if (!destination) return

    const sourceKey = source.droppableId as StarStatus
    const destinationKey = destination.droppableId as StarStatus

    if (sourceKey === destinationKey) return

    const _stars = JSON.parse(JSON.stringify(bronzeStars)) as typeof bronzeStars
    const [targetStar] = _stars[sourceKey].splice(source.index, 1)
    _stars[destinationKey].splice(destination.index, 0, targetStar)
    setBronzeStars(_stars)
  }

  useEffect(() => {
    console.log(goldStars)
  }, [goldStars])

  useEffect(() => {
    // dnd 애니메이션을 위한 준비
    const animation = requestAnimationFrame(() => setEnabled(true))

    return () => {
      cancelAnimationFrame(animation)
      setEnabled(false)
    }
  }, [])

  if (!enabled) return null

  return (
    <S.CombinationGsbContainer>
      <S.DragAndDropArea onDragEnd={onDragEndGold}>
        <DragAndDrop stars={goldStars} starClass={'gold'} />
      </S.DragAndDropArea>
      <S.DragAndDropArea onDragEnd={onDragEndSilver}>
        <DragAndDrop stars={silverStars} starClass={'silver'} />
      </S.DragAndDropArea>
      <S.DragAndDropArea onDragEnd={onDragEndBronze}>
        <DragAndDrop stars={bronzeStars} starClass={'bronze'} />
      </S.DragAndDropArea>

      <S.SubmitButton>
        <CButton
          color={colors.greyScale.white}
          text="조합"
          backgroundColor={colors.button.purple}
          radius={100}
          width={118}
          fontSize={20}
        />
      </S.SubmitButton>
    </S.CombinationGsbContainer>
  )
}

export default CombinationGsb
