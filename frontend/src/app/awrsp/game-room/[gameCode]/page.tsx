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
import { TimerState } from '@atom/awrspAtom'

const AwrspGameRoom = () => {
  const { Modal, closeModal, isOpen, openModal } = useModal()
  const timerTime = useRecoilValue(TimerState)

  useEffect(() => {
    openModal()
  }, [])

  return (
    <S.AwrspGameRoomContainer>
      <S.RoundDisplay>14 Round</S.RoundDisplay>
      <S.Content>
        <RspCombination />
        {/* <MyResult /> */}
        {/* <AllResultList /> */}
      </S.Content>
      <S.TimerContainer>
        <Timer fontSize="16" size="96" time={timerTime} />
      </S.TimerContainer>
      <Modal isOpen={isOpen}>
        <DrawCardModal closeModal={closeModal} />
      </Modal>
    </S.AwrspGameRoomContainer>
  )
}

export default withAuth(AwrspGameRoom)
