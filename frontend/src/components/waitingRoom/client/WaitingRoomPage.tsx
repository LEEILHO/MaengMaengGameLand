'use client'

import React, { useCallback, useEffect, useState } from 'react'

import * as S from '@styles/waitingRoom/WaitingRoom.styled'
import spaceAnimation from 'assets/lotties/background.json'
import WaitingRoomHeader from './WaitingRoomHeader'
import PlayerCard from './PlayerCard'
import CButton from '@components/common/clients/CButton'
import { colors } from '@constants/colors'
import Chatting from './Chatting'
import { SeatInfo } from '@type/waitingRoom/seat.type'
import useModal from '@hooks/useModal'
import UpdateRoomModal from './UpdateRoomModal'
import BackButton from '@components/common/clients/BackButton'
import { usePathname, useRouter } from 'next/navigation'
import { useRecoilValue, useResetRecoilState } from 'recoil'
import { RoomInfoState } from '@atom/waitingRoomAtom'
import useSocketWaitingRoom from '@hooks/useSocketWaitingRoom'
import { userState } from '@atom/userAtom'
import useSound from '@hooks/useSound'
import AlertModal from '@components/gsb/client/AlertModal'
import useDidMountEffect from '@hooks/useDidMoundEffect'
import { ChatListState } from '@atom/chatAtom'

const WaitingRoomPage = () => {
  const router = useRouter()
  const roomId = usePathname().split('/')[3]
  const gameType = usePathname().split('/')[1]
  const user = useRecoilValue(userState)
  const {
    connectSocket,
    disconnectSocket,
    connectWaitingRoom,
    disconnectWaitingRoom,
    handleEmptySeat,
    handleExit,
    handleReady,
    handleGameStart,
    handleSendChat,
    handleUpdateRoom,
    handleKick,
    roomInfo,
  } = useSocketWaitingRoom()

  const { playButtonSound } = useSound()
  const { Modal, isOpen, openModal, closeModal } = useModal()
  const {
    Modal: AModal,
    isOpen: isAlertOpen,
    openModal: openAlertModal,
    closeModal: closeAlertModal,
  } = useModal()
  const [alertText, setAlertText] = useState('')
  const [seats, setSeats] = useState<SeatInfo[]>([])
  const [isHost, setIsHost] = useState(false)
  const [isReady, setIsReady] = useState(false)

  const resetChat = useResetRecoilState(ChatListState)

  const handleBack = useCallback(() => {
    resetChat()
    handleExit()
    router.replace(`/${gameType}/lobby`)
  }, [])

  const onClickGameSetting = useCallback(() => {
    playButtonSound()
    openModal()
  }, [])

  const onClickGameStart = useCallback(() => {
    playButtonSound()
    handleGameStart()

    // if (!roomInfo) return
    // console.log(gameType, roomInfo.headCount)
    // if (gameType === 'gsb' && roomInfo?.headCount !== 2) {
    //   setAlertText('금은동 게임은 2인 게임입니다.')
    //   openAlertModal()
    // } else if (gameType === 'jwac' && roomInfo?.headCount < 4) {
    //   setAlertText('무제한 보석 경매는 4인 이상 게임입니다.')
    //   openAlertModal()
    // } else if (gameType === 'awrsp' && roomInfo.headCount === 1) {
    //   setAlertText('전승 가위바위보는 2인 이상 게임입니다.')
    //   openAlertModal()
    // } else handleGameStart()
  }, [])

  const onClickReady = useCallback(() => {
    playButtonSound()
    handleReady()
  }, [])

  useEffect(() => {
    if (roomId) {
      connectSocket(connectWaitingRoom, disconnectWaitingRoom)
    }

    return () => {
      disconnectSocket()
    }
  }, [roomId])

  useEffect(() => {
    if (!roomInfo) return
    setSeats(roomInfo?.participant)
  }, [roomInfo])

  useEffect(() => {
    if (seats) {
      // 내가 방장인지 아닌지, 강퇴당하였는지 체크
      let isKicked = true
      seats.map((seat) => {
        if (seat.user) {
          if (seat.user.nickname === user?.nickname) {
            isKicked = false
            if (seat.user.ready) setIsReady(true)
            else setIsReady(false)
          }
          if (seat.user.host) {
            if (seat.user.nickname === user?.nickname) {
              setIsHost(true)
              return
            }
          }
        }
      })

      if (isKicked) router.replace(`/${gameType}/lobby`)
    }
  }, [seats])

  return (
    <>
      <S.WaitingRoomContainer>
        <WaitingRoomHeader
          publicRoom={roomInfo ? roomInfo?.publicRoom : true}
          title={roomInfo ? roomInfo.title : ''}
        />
        <S.Background animationData={spaceAnimation} loop />
        <S.Contents>
          <Chatting handleSendChat={handleSendChat} />
          <S.PlayerList>
            {seats.map((seat, index) => (
              <PlayerCard
                user={seat.user}
                isOpened={seat.open}
                isHost={isHost}
                index={index}
                handleEmptySeat={handleEmptySeat}
                handleKick={handleKick}
                key={index}
              />
            ))}
          </S.PlayerList>
        </S.Contents>
        <S.BottomButtons>
          <BackButton size={44} handleBack={handleBack} />
          <S.ButtonRelatedGame>
            {isHost ? (
              <>
                <CButton
                  width={118}
                  height={48}
                  radius={109}
                  fontSize={20}
                  color={colors.greyScale.white}
                  text="게임 설정"
                  backgroundColor={colors.greyScale.grey400}
                  onClick={onClickGameSetting}
                />
                <CButton
                  width={118}
                  height={48}
                  radius={109}
                  fontSize={20}
                  color={colors.greyScale.white}
                  text="게임 시작"
                  backgroundColor={colors.button.purple}
                  onClick={onClickGameStart}
                />
              </>
            ) : isReady ? (
              <CButton
                width={118}
                height={48}
                radius={109}
                fontSize={20}
                color={colors.greyScale.white}
                text="준비 완료"
                backgroundColor={colors.greyScale.grey400}
                onClick={onClickReady}
              />
            ) : (
              <CButton
                width={118}
                height={48}
                radius={109}
                fontSize={20}
                color={colors.greyScale.white}
                text="준비"
                backgroundColor={colors.button.purple}
                onClick={onClickReady}
              />
            )}
          </S.ButtonRelatedGame>
        </S.BottomButtons>
      </S.WaitingRoomContainer>

      <Modal isOpen={isOpen}>
        <UpdateRoomModal
          handleUpdateRoom={handleUpdateRoom}
          closeModal={closeModal}
        />
      </Modal>

      <AModal isOpen={isAlertOpen} closeModal={closeAlertModal}>
        <AlertModal text={alertText} closeModal={closeAlertModal} />
      </AModal>
    </>
  )
}

export default WaitingRoomPage
