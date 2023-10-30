'use client'

import React, { useEffect, useState } from 'react'

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

const user = {
  userSeq: 1,
  nickname: '맹박사',
  host: true,
  ready: false,
  isClose: false,
}

const WaitingRoomPage = () => {
  const { Modal, isOpen, openModal, closeModal } = useModal()
  const [isHost, setIsHost] = useState(false)
  const [dummyParticipant, setDummyParticipant] = useState<SeatInfo[]>([
    {
      userSeq: 1,
      nickname: '맹박사',
      host: true,
      ready: false,
      isClose: false,
    },
    {
      userSeq: 2,
      nickname: '멍박사',
      host: false,
      ready: true,
      isClose: false,
    },
    {
      userSeq: 3,
      nickname: '댕박사',
      host: false,
      ready: false,
      isClose: false,
    },
    {
      userSeq: 4,
      nickname: '뱅박사',
      host: false,
      ready: false,
      isClose: false,
    },
    {
      userSeq: 0,
      nickname: '',
      host: false,
      ready: false,
      isClose: false,
    },
    {
      userSeq: 0,
      nickname: '',
      host: false,
      ready: false,
      isClose: false,
    },
    {
      userSeq: 0,
      nickname: '',
      host: false,
      ready: false,
      isClose: true,
    },
    {
      userSeq: 0,
      nickname: '',
      host: false,
      ready: false,
      isClose: true,
    },
  ])

  const onClickEmptySeat = (index: number) => {
    let newDummyParticipant = [...dummyParticipant]
    newDummyParticipant.map((participant, newIndex) => {
      if (newIndex === index) participant.isClose = !participant.isClose
    })
    setDummyParticipant(newDummyParticipant)
  }

  useEffect(() => {
    // authHttp.get(`v1/user/info`).then((res) => {
    //   console.log(res)
    // })
    dummyParticipant.map((participant) => {
      if (participant.host) {
        if (participant.userSeq === user.userSeq) {
          setIsHost(true)
        }
      }
    })
  }, [])

  return (
    <>
      <S.WaitingRoomContainer>
        <WaitingRoomHeader publicRoom={false} />
        <S.Background animationData={spaceAnimation} loop />
        <S.Contents>
          <Chatting />
          <S.PlayerList>
            {dummyParticipant.map((participant, index) => (
              <PlayerCard
                user={participant}
                index={index}
                onClickEmptySeat={onClickEmptySeat}
                key={index}
              />
            ))}
          </S.PlayerList>
        </S.Contents>
        <S.BottomButtons>
          <BackButton size={32} />
          <S.ButtonRelatedGame>
            <CButton
              radius={109}
              fontSize={20}
              color={colors.greyScale.white}
              text="게임 설정"
              backgroundColor={colors.greyScale.grey400}
              onClick={openModal}
            />
            {isHost ? (
              <CButton
                radius={109}
                fontSize={20}
                color={colors.greyScale.white}
                text="게임 시작"
                backgroundColor={colors.button.purple}
              />
            ) : (
              <CButton
                radius={109}
                fontSize={20}
                color={colors.greyScale.white}
                text="게임 준비"
                backgroundColor={colors.button.purple}
              />
            )}
          </S.ButtonRelatedGame>
        </S.BottomButtons>
      </S.WaitingRoomContainer>

      <Modal isOpen={isOpen}>
        <UpdateRoomModal closeModal={closeModal} />
      </Modal>
    </>
  )
}

export default WaitingRoomPage
