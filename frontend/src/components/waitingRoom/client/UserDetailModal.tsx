'use client'

import React, { useEffect } from 'react'

import * as S from '@styles/waitingRoom/UserDetailModal.styled'
import { images } from '@constants/images'
import CButton from '@components/common/clients/CButton'
import { colors } from '@constants/colors'

type Props = {
  userSeq: number | undefined
}

const UserDetailModal = ({ userSeq }: Props) => {
  useEffect(() => {
    // 유저정보 가져오는 코드
  }, [])

  return (
    <S.UserDetailModalContainer>
      <S.UserProfile src={images.waitingRoom.dummyRabbit} alt="프로필사진" />
      <S.UserDetail>
        <tr>
          <td>Name</td>
          <td className="info">토오끼</td>
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
          color="white"
          radius={100}
          text="친구 추가"
          backgroundColor={colors.button.blue}
        />
        <CButton
          color="white"
          radius={100}
          text="강퇴"
          backgroundColor={colors.button.red}
        />
      </S.BottomButtons>
    </S.UserDetailModalContainer>
  )
}

export default UserDetailModal
