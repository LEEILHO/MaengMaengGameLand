'use client'

import * as S from '@styles/awrsp/AwrspGameRoom.styled'
import withAuth from '@components/hoc/client/PrivateRoute'
import { images } from '@constants/images'
import Timer from '@components/common/clients/Timer'
import RspCombination from '@components/awrsp/client/RspCombination'
import useModal from '@hooks/useModal'
import DrawCardModal from '@components/awrsp/client/DrawCardModal'
import { useEffect } from 'react'
import MyResult from '@components/awrsp/client/MyResult'
import AllResultList from '@components/awrsp/client/AllResultList'
import { useRecoilValue } from 'recoil'
import { RoundState, StepState, TimerState } from '@atom/awrspAtom'
import useSocketAWRSP from '@hooks/useSocketAWRSP'
import useDidMountEffect from '@hooks/useDidMoundEffect'

const AwrspGameRoom = () => {
  const {
    connectSocket,
    disconnectSocket,
    handleRoundStart,
    handleTimeOver,
    handleCardSubmit,
  } = useSocketAWRSP()
  const { Modal, closeModal, isOpen, openModal } = useModal()
  const timerTime = useRecoilValue(TimerState)
  const step = useRecoilValue(StepState)
  const round = useRecoilValue(RoundState)

  const timeOverHandle = () => {
    if (step) {
      console.log(step, '종료')
      handleTimeOver(step)
    }
  }

  useEffect(() => {
    connectSocket()

    return () => {
      disconnectSocket()
    }
  }, [])

  useEffect(() => {
    openModal()
  }, [step])

  return (
    <S.AwrspGameRoomContainer>
      <S.RoundDisplay>{round} Round</S.RoundDisplay>
      <S.Content>
        {step === 'CARD_SUBMIT' && (
          <RspCombination handleCardSubmit={handleCardSubmit} />
        )}
        {step === 'PLAYER_WINS' && <MyResult />}
        {step === 'ALL_WINS' && <AllResultList />}
      </S.Content>
      <S.TimerContainer>
        <Timer
          fontSize="16"
          size="96"
          time={timerTime}
          round={round}
          timeOverHandle={timeOverHandle}
        />
      </S.TimerContainer>

      <Modal isOpen={isOpen}>
        <DrawCardModal closeModal={closeModal} />
      </Modal>
    </S.AwrspGameRoomContainer>
  )
}

export default withAuth(AwrspGameRoom)
