'use client'

import { useCallback } from 'react'
import * as S from '@styles/waitingRoom/Header.styled'
import { images } from '@constants/images'
import useModal from '@hooks/useModal'
import useSound from '@hooks/useSound'
import SettingModal from '@components/home/SettingModal'

type Props = {
  publicRoom: boolean
  title: string
}

const WaitingRoomHeader = ({ publicRoom, title }: Props) => {
  const {
    Modal: SModal,
    isOpen: isSettingOpen,
    openModal: openSettingModal,
    closeModal: closeSettingModal,
  } = useModal()
  const { playButtonSound } = useSound()

  const onClickSetting = useCallback(() => {
    playButtonSound()
    openSettingModal()
  }, [])
  return (
    <S.HeaderContainer>
      <S.RoomInfo>
        {publicRoom ? (
          <img src={images.waitingRoom.header.unlock} alt="공개방" />
        ) : (
          <img src={images.waitingRoom.header.lock} alt="비공개방" />
        )}
        <p>{title}</p>
      </S.RoomInfo>
      <S.SettingButton onClick={onClickSetting}>
        <img src={images.common.header.setting} alt="setting" />
      </S.SettingButton>

      <SModal isOpen={isSettingOpen} closeModal={closeSettingModal}>
        <SettingModal closeModal={closeSettingModal} />
      </SModal>
    </S.HeaderContainer>
  )
}

export default WaitingRoomHeader
