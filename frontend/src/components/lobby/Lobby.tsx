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
import { useRecoilState, useRecoilValue, useSetRecoilState } from 'recoil'
import { channelState, gameTypeState } from '@atom/gameAtom'
import { usePathname, useRouter } from 'next/navigation'
import { gameTypeChange } from '@utils/lobby/lobbyUtil'
import { roomsState } from '@atom/lobbyAtom'
import useSound from '@hooks/useSound'

type Props = {
  title: string
}

const Lobby = ({ title }: Props) => {
  const router = useRouter()
  const pathname = usePathname()
  const { Modal, isOpen, closeModal, openModal } = useModal()
  const { playButtonSound } = useSound()
  const { connectSocket, disconnectSocket, connectLobby, disconnectLobby } =
    useSocket()
  const [gameType, setGameType] = useRecoilState(gameTypeState)
  const channel = useRecoilValue(channelState)
  const setRooms = useSetRecoilState(roomsState)
  const gamePath = pathname.split('/')[1]

  const handleBack = useCallback(() => {
    setRooms([])
    router.replace('/home')
  }, [])

  const handleCreateRoom = () => {
    playButtonSound()
    openModal()
  }

  useEffect(() => {
    // @ts-ignore
    navigator.virtualKeyboard.overlaysContent = true
  })

  useEffect(() => {
    setGameType(gameTypeChange(gamePath))
  }, [pathname])

  useEffect(() => {
    connectSocket(connectLobby, disconnectLobby)

    return () => {
      disconnectSocket()
    }
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
          <BackButton size={44} handleBack={handleBack} />
          <CButton
            text="방 생성"
            backgroundColor="rgba(0, 163, 255, 1)"
            fontSize={18}
            color="white"
            radius={50}
            onClick={handleCreateRoom}
          />
        </S.ButtonRow>
      </S.LobbyContainer>
    </>
  )
}

export default Lobby
