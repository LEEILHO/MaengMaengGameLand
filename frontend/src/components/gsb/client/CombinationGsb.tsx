'use client'

import * as S from '@styles/gsb/CombinationGsb.styled'
import { images } from '@constants/images'
import { StarListType, StarStatus } from '@type/gsb/gsb.type'
import { useEffect, useState } from 'react'
import { DropResult } from 'react-beautiful-dnd'
import DragAndDrop from './DragAndDrop'
import CButton from '@components/common/clients/CButton'
import { colors } from '@constants/colors'
import useModal from '@hooks/useModal'
import { useRecoilValue } from 'recoil'
import { MyState, OpponentState } from '@atom/gsbAtom'
import AlertModal from './AlertModal'

type Props = {
  handleGSBComb: (gold: number, silver: number, bronze: number) => void
}

const CombinationGsb = ({ handleGSBComb }: Props) => {
  const { Modal, isOpen, closeModal, openModal } = useModal()
  const my = useRecoilValue(MyState)
  const opponent = useRecoilValue(OpponentState)
  const [enabled, setEnabled] = useState(false)
  const [goldStars, setGoldStars] = useState<StarListType>({
    in: [],
    out: [...Array(my?.currentGold)].map((_, i) => ({
      id: `gold${i}`,
      src: images.gsb.goldStar,
      status: 'out',
    })),
  })

  const [silverStars, setSilverStars] = useState<StarListType>({
    in: [],
    out: [...Array(my?.currentSilver)].map((_, i) => ({
      id: `silver${i}`,
      src: images.gsb.silverStar,
      status: 'out',
    })),
  })

  const [bronzeStars, setBronzeStars] = useState<StarListType>({
    in: [],
    out: [...Array(my?.currentBronze)].map((_, i) => ({
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

  const onClickSubmit = () => {
    const gold = goldStars['in'].length
    const silver = silverStars['in'].length
    const bronze = bronzeStars['in'].length

    const weight = gold * 3 + silver * 2 + bronze

    if (opponent) {
      if (
        opponent.currentWeight !== 0 &&
        (opponent.currentWeight + 2 < weight ||
          opponent.currentWeight - 2 > weight)
      ) {
        openModal()
        return
      }

      if (opponent.currentWeight === 0 && (weight < 4 || weight > 12)) {
        openModal()
        return
      }
    }

    handleGSBComb(gold, silver, bronze)
  }

  useEffect(() => {
    console.log('my: ', my)
    console.log('opponent: ', opponent)
  }, [])

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
    <>
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
            onClick={onClickSubmit}
          />
        </S.SubmitButton>
      </S.CombinationGsbContainer>

      <Modal isOpen={isOpen} closeModal={closeModal}>
        <AlertModal
          text={'제출할 수 없는 조합입니다.'}
          closeModal={closeModal}
        />
      </Modal>
    </>
  )
}

export default CombinationGsb
