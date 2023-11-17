import React, { ChangeEvent, useEffect, useState, useCallback } from 'react'

import * as S from '@styles/waitingRoom/UpdateRoomModal.styled'
import CButton from '@components/common/clients/CButton'
import { colors } from '@constants/colors'
import { useRecoilValue } from 'recoil'
import { RoomInfoState } from '@atom/waitingRoomAtom'
import useSocketWaitingRoom from '@hooks/useSocketWaitingRoom'
import useSound from '@hooks/useSound'

type Props = {
  closeModal: () => void
  handleUpdateRoom: (title: string, isPublic: boolean) => void
}

const UpdateRoomModal = ({ closeModal, handleUpdateRoom }: Props) => {
  const { playButtonSound } = useSound()
  const roomInfo = useRecoilValue(RoomInfoState)
  const [isPublic, setIsPublic] = useState<boolean>(true)
  const [title, setTitle] = useState<string>('')

  const onTogglePublicButton = () => {
    setIsPublic(!isPublic)
  }

  const onChangeTitle = (e: ChangeEvent<HTMLInputElement>) => {
    setTitle(e.target.value)
  }

  const onClickUpdateButton = () => {
    if (title) {
      playButtonSound()
      handleUpdateRoom(title, isPublic)
      closeModal()
    }
  }

  const onClickClose = useCallback(() => {
    playButtonSound()
    closeModal()
  }, [])

  useEffect(() => {
    if (roomInfo) {
      setTitle(roomInfo?.title)
      setIsPublic(roomInfo?.publicRoom)
    }
  }, [roomInfo])

  return (
    <S.UpdateRoomModalContainer>
      <S.TopRow>
        <S.Title>게임 설정</S.Title>
      </S.TopRow>
      <S.SubRow>
        <S.SubTitle>방제목</S.SubTitle>
        <S.RoomNameInput type="text" value={title} onChange={onChangeTitle} />
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
          onClick={onClickUpdateButton}
        />
        <CButton
          width={85}
          height={36}
          fontSize={16}
          text="취소"
          color="white"
          backgroundColor={colors.button.red}
          radius={50}
          onClick={onClickClose}
        />
      </S.ButtonRow>
    </S.UpdateRoomModalContainer>
  )
}

export default UpdateRoomModal
