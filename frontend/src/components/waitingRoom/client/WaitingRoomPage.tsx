'use client'

import React, { useCallback, useEffect, useState } from 'react'

import * as S from '@styles/waitingRoom/WaitingRoom.styled'
import spaceAnimation from 'assets/lotties/background.json'
import { authHttp } from '@utils/http'
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
import { useRecoilState, useRecoilValue } from 'recoil'
import { RoomInfoState } from '@atom/waitingRoomAtom'
import useSocketWaitingRoom from '@hooks/useSocketWaitingRoom'
import { userState } from '@atom/userAtom'

const WaitingRoomPage = () => {
  const router = useRouter()
  const roomId = usePathname().split('/')[3]
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
  } = useSocketWaitingRoom()

  const roomInfo = useRecoilValue(RoomInfoState)

  const { Modal, isOpen, openModal, closeModal } = useModal()
  const [seats, setSeats] = useState<SeatInfo[]>([])
  const [isHost, setIsHost] = useState(false)
  const [mySeatNumber, setMySeatNumber] = useState<number>(0)

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
      // 내가 방장인지 아닌지 체크
      seats.map((seat, index) => {
        if (seat.user) {
          if (seat.user.nickname === user?.nickname) setMySeatNumber(index)
          if (seat.user.host) {
            if (seat.user.nickname === user?.nickname) {
              setIsHost(true)
              return
            }
          }
        }
      })
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
                onClickEmptySeat={handleEmptySeat}
                handleKick={handleKick}
                key={index}
              />
            ))}
          </S.PlayerList>
        </S.Contents>
        <S.BottomButtons>
          <BackButton size={44} handleBack={handleExit} />
          <S.ButtonRelatedGame>
            {isHost ? (
              <>
                <CButton
                  height={48}
                  radius={109}
                  fontSize={20}
                  color={colors.greyScale.white}
                  text="게임 설정"
                  backgroundColor={colors.greyScale.grey400}
                  onClick={openModal}
                />
                <CButton
                  height={48}
                  radius={109}
                  fontSize={20}
                  color={colors.greyScale.white}
                  text="게임 시작"
                  backgroundColor={colors.button.purple}
                  onClick={handleGameStart}
                />
              </>
            ) : (
              <CButton
                height={48}
                radius={109}
                fontSize={20}
                color={colors.greyScale.white}
                text="게임 준비"
                backgroundColor={colors.button.purple}
                onClick={handleReady}
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
    </>
  )
}

export default WaitingRoomPage
