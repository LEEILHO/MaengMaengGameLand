'use client'

import BackButton from '@components/common/clients/BackButton'
import CButton from '@components/common/clients/CButton'
import RoomList from '@components/lobby/RoomList'
import useModal from '@hooks/useModal'
import * as S from '@styles/lobby/Lobby.styled'
import Background from 'assets/lotties/background.json'
import { useCallback, useEffect } from 'react'
import CreateRoomModal from './CreateRoomModal'
import useSocket from '@hooks/useSocketLobby'
import { useRecoilState, useRecoilValue } from 'recoil'
import { channelState, gameTypeState } from '@atom/gameAtom'
import { usePathname } from 'next/navigation'
import { gameTypeChange } from '@utils/lobby/lobbyUtil'

type Props = {
  title: string
}

const Lobby = ({ title }: Props) => {
  const pathname = usePathname()
  const { Modal, isOpen, closeModal, openModal } = useModal()
  const { connectSocket, disconnectSocket } = useSocket()
  const [gameType, setGameType] = useRecoilState(gameTypeState)
  const channel = useRecoilValue(channelState)

  useEffect(() => {
    setGameType(gameTypeChange(pathname.split('/')[1]))
  }, [pathname])

  useEffect(() => {
    connectSocket()

    return () => {
      disconnectSocket()
    }
  }, [gameType, channel])

  useEffect(() => {
    console.log(gameType, channel)
  }, [gameType, channel])

  return (
    <>
      <Modal isOpen={isOpen}>
        <CreateRoomModal closeModal={closeModal} />
      </Modal>
      <S.LobbyContainer>
        <S.BackgroundLottie animationData={Background} loop />
        <S.Title>{title}</S.Title>
        <S.RoomListContainer>
          <RoomList />
        </S.RoomListContainer>
        <S.ButtonRow>
          <BackButton size={44} />
          <CButton
            text="방 생성"
            backgroundColor="rgba(0, 163, 255, 1)"
            fontSize={18}
            color="white"
            radius={50}
            onClick={openModal}
          />
        </S.ButtonRow>
      </S.LobbyContainer>
    </>
  )
}

export default Lobby
