import React, { useState } from 'react'

import * as S from '@styles/waitingRoom/UpdateRoomModal.styled'
import CButton from '@components/common/clients/CButton'
import { colors } from '@constants/colors'

type Props = {
  closeModal: () => void
}

const UpdateRoomModal = ({ closeModal }: Props) => {
  const [isPublic, setIsPublic] = useState(false)

  const onTogglePublicButton = () => {
    setIsPublic(!isPublic)
  }
  return (
    <S.UpdateRoomModalContainer>
      <S.TopRow>
        <S.Title>게임 설정</S.Title>
      </S.TopRow>
      <S.SubRow>
        <S.SubTitle>방제목</S.SubTitle>
        <S.RoomNameInput type="text" value={'제목 제목 제목 제목 제목'} />
      </S.SubRow>
      <S.SubRow>
        <S.SubTitle>공개 설정</S.SubTitle>
        <S.PublicButtonContainer>
          <S.CheckBox
            id={'toggleBtn'}
            type="checkbox"
            onChange={onTogglePublicButton}
          />
          <S.CheckBoxLable htmlFor="toggleBtn" isPublic={isPublic} />
        </S.PublicButtonContainer>
      </S.SubRow>
      <S.ButtonRow>
        <CButton
          width={85}
          height={36}
          fontSize={16}
          text="설정"
          color="white"
          backgroundColor={colors.button.blue}
          radius={50}
        />
        <CButton
          width={85}
          height={36}
          fontSize={16}
          text="취소"
          color="white"
          backgroundColor={colors.button.red}
          radius={50}
          onClick={closeModal}
        />
      </S.ButtonRow>
    </S.UpdateRoomModalContainer>
  )
}

export default UpdateRoomModal
