'use client'

import { images } from '@constants/images'
import useModal from '@hooks/useModal'
import * as S from '@styles/gsb/RoundResult.styled'
import { useEffect } from 'react'

const RoundResult = () => {
  const { isOpen, closeModal, Modal, openModal } = useModal()

  useEffect(() => {
    // 포기했을 때는 모달 바로 뜨게
    setTimeout(() => {
      openModal()
      setTimeout(() => {
        closeModal()
      }, 3000)
    }, 5000)
  }, [])

  // framer motion variants
  const combAreaVariants = {
    hidden: { opacity: 1, scale: 0 },
    visible: {
      opacity: 1,
      scale: 1,
      transition: {
        delayChildren: 1,
        staggerChildren: 1,
      },
    },
  }

  const itemVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
    },
  }

  const gold = [...Array(1)]
  const silver = [...Array(3)]
  const bronze = [...Array(12)]
  return (
    <S.RoundResultContainer>
      {/* 포기 안 했을 때 */}
      <S.CombArea
        variants={combAreaVariants}
        initial="hidden"
        animate="visible"
      >
        <S.StarList variants={itemVariants}>
          {bronze.map((_, index) => (
            <S.Star
              key={`bronze${index}`}
              src={images.gsb.bronzeStar}
              index={index}
              reverse={'false'}
            />
          ))}
        </S.StarList>
        <S.StarList variants={itemVariants}>
          {silver.map((_, index) => (
            <S.Star
              key={`silver${index}`}
              src={images.gsb.silverStar}
              index={index}
              reverse={'false'}
            />
          ))}
        </S.StarList>
        <S.StarList variants={itemVariants}>
          {gold.map((_, index) => (
            <S.Star
              key={`gold${index}`}
              src={images.gsb.goldStar}
              index={index}
              reverse={'false'}
            />
          ))}
        </S.StarList>
      </S.CombArea>

      <S.CombArea
        variants={combAreaVariants}
        initial="hidden"
        animate="visible"
      >
        <S.StarList variants={itemVariants}>
          {bronze.map((_, index) => (
            <S.Star
              key={index}
              src={images.gsb.bronzeStar}
              index={index}
              reverse={'true'}
            />
          ))}
        </S.StarList>
        <S.StarList variants={itemVariants}>
          {silver.map((_, index) => (
            <S.Star
              key={index}
              src={images.gsb.silverStar}
              index={index}
              reverse={'true'}
            />
          ))}
        </S.StarList>
        <S.StarList variants={itemVariants}>
          {gold.map((_, index) => (
            <S.Star
              key={index}
              src={images.gsb.goldStar}
              index={index}
              reverse={'true'}
            />
          ))}
        </S.StarList>
      </S.CombArea>

      <Modal isOpen={isOpen}>
        <S.RoundResultModalContainer>
          {/* <S.ModalContent>심은진님이 포기하였습니다.</S.ModalContent> */}
          <S.ModalContent>김진영님이 {10}개의 칩을 가져갑니다.</S.ModalContent>
        </S.RoundResultModalContainer>
      </Modal>
    </S.RoundResultContainer>
  )
}

export default RoundResult
