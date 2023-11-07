'use client'

import React, { useEffect } from 'react'

import * as S from '@styles/waitingRoom/UserDetailModal.styled'
import { images } from '@constants/images'
import CButton from '@components/common/clients/CButton'
import { colors } from '@constants/colors'

type Props = {
  nickname: string
  isHost: boolean
  handleKick: (kickPlayer: string) => void
  closeModal: () => void
}

const UserDetailModal = ({
  nickname,
  isHost,
  handleKick,
  closeModal,
}: Props) => {
  useEffect(() => {
    // 유저정보 가져오는 코드
  }, [])

  return (
    <S.UserDetailModalContainer>
      <S.CloseButton src={images.lobby.close} onClick={closeModal} />
      <S.UserProfile src={images.waitingRoom.dummyRabbit} alt="프로필사진" />
      <S.UserDetail>
        <tr>
          <td>Name</td>
          <td className="info">{nickname}</td>
        </tr>
        <tr>
          <td>Winrate</td>
          <td className="info">33%</td>
        </tr>
        <tr>
          <td>Rating</td>
          <td className="info">1,200</td>
        </tr>
      </S.UserDetail>
      <S.BottomButtons>
        <CButton
          height={36}
          fontSize={16}
          color="white"
          radius={100}
          text="친구 추가"
          backgroundColor={colors.button.blue}
        />
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
