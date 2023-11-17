'use client'

import React, { useEffect } from 'react'

import * as S from '@styles/waitingRoom/UserDetailModal.styled'
import { images } from '@constants/images'
import CButton from '@components/common/clients/CButton'
import { colors } from '@constants/colors'
import { useCallback } from 'react'
import useSound from '@hooks/useSound'

type Props = {
  profileUrl: string
  nickname: string
  isHost: boolean
  handleKick: (kickPlayer: string) => void
  closeModal: () => void
}

const UserDetailModal = ({
  profileUrl,
  nickname,
  isHost,
  handleKick,
  closeModal,
}: Props) => {
  const { playButtonSound } = useSound()
  const onClickCloseButton = useCallback(() => {
    playButtonSound()
    closeModal()
  }, [])

  return (
    <S.UserDetailModalContainer>
      <S.CloseButton src={images.lobby.close} onClick={onClickCloseButton} />
      <S.UserDetail>
        <S.UserProfile src={profileUrl} alt="프로필사진" />
        <S.Nickname>{nickname}</S.Nickname>
      </S.UserDetail>
      <S.BottomButtons>
        {/* <CButton
          height={36}
          fontSize={16}
          color="white"
          radius={100}
          text="친구 추가"
          backgroundColor={colors.button.blue}
        /> */}
        {isHost && (
          <CButton
            height={36}
            fontSize={16}
            color="white"
            radius={100}
            text="강퇴"
            backgroundColor={colors.button.red}
            onClick={() => handleKick(nickname)}
          />
        )}
      </S.BottomButtons>
    </S.UserDetailModalContainer>
  )
}

export default UserDetailModal
