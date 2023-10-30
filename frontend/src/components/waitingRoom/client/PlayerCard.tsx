'use client'

import React, { useState } from 'react'
import * as S from '@styles/waitingRoom/PlayerCard.styled'
import { images } from '@constants/images'
import useModal from '@hooks/useModal'
import UserDetailModal from './UserDetailModal'

type Props = {
  user?: {
    userSeq: number
    nickname: string
    host: boolean
    ready: boolean
    isClose: boolean
  }
  onClickEmptySeat: (index: number) => void
  index: number
}

const PlayerCard = ({ user, onClickEmptySeat, index }: Props) => {
  const { Modal, isOpen, openModal, closeModal } = useModal()

  // 유저 칸이 닫혀 있는지 아닌지 확인
  return !user?.isClose ? (
    // 열려 있을 때, 칸을 차지한 유저가 있다면
    user?.userSeq !== 0 ? (
      <>
        <S.PlayerCardContainer>
          {user?.host && (
            <S.ManagerMark src={images.waitingRoom.crown} alt="방장" />
          )}
          {!user?.host && user?.ready && (
            <S.ReadyMark src={images.waitingRoom.ready} alt="레디" />
          )}
          <S.UserInfo>
            <S.TierFrame>
              <img
                className="frame"
                src={images.common.header.goldFrame}
                alt="티어"
              />
              <S.ProfileImage
                src={images.common.header.dummyProfile}
                alt="프로필사진"
              />
            </S.TierFrame>

            <S.UserNickname>{user?.nickname}</S.UserNickname>
          </S.UserInfo>
          <S.UserDetailButton onClick={openModal}>
            <img src={images.waitingRoom.info} alt="userInformation" />
          </S.UserDetailButton>
        </S.PlayerCardContainer>

        {/* 유저의 디테일한 정보를 보여주는 모달 */}
        <Modal isOpen={isOpen} closeModal={closeModal}>
          <UserDetailModal userSeq={user?.userSeq} />
        </Modal>
      </>
    ) : (
      // 비어 있는 칸이라면
      <S.EmptyPlayerCardContainer
        onClick={() => {
          onClickEmptySeat(index)
        }}
      ></S.EmptyPlayerCardContainer>
    )
  ) : (
    <S.EmptyPlayerCardContainer
      onClick={() => {
        onClickEmptySeat(index)
      }}
    >
      <S.InActiveMark src={images.waitingRoom.inactive} alt="잠김" />
    </S.EmptyPlayerCardContainer>
  )
}

export default PlayerCard
