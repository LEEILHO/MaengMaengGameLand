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
import {
  GameResultState,
  RoundState,
  StepState,
  TimerState,
} from '@atom/awrspAtom'
import useSocketAWRSP from '@hooks/useSocketAWRSP'
import useDidMountEffect from '@hooks/useDidMoundEffect'
import HistoryModal from '@components/awrsp/client/HistoryModal'
import { useRouter } from 'next/navigation'

const AwrspGameRoom = () => {
  const router = useRouter()
  const {
    connectSocket,
    disconnectSocket,
    handleRoundStart,
    handleTimeOver,
    handleCardSubmit,
  } = useSocketAWRSP()
  const { Modal, closeModal, isOpen, openModal } = useModal()
  const {
    Modal: HModal,
    closeModal: closeHistoryModal,
    isOpen: isHistoryOpen,
    openModal: openHistoryModal,
  } = useModal()
  const timerTime = useRecoilValue(TimerState)
  const step = useRecoilValue(StepState)
  const round = useRecoilValue(RoundState)
  const gameResult = useRecoilValue(GameResultState)

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
    if (step === 'DRAW_CARD') openModal()
  }, [step])

  return (
    <S.AwrspGameRoomContainer>
      {step !== 'GAME_OVER' ? (
        // 게임이 종료되지 않았다면
        <>
          <S.RoundDisplay>{round} Round</S.RoundDisplay>
          <S.Content>
            {step === 'CARD_SUBMIT' && (
              <RspCombination handleCardSubmit={handleCardSubmit} />
            )}
            {step === 'PLAYER_WINS' && <MyResult />}
            {(step === 'ALL_WINS' || step === 'WAITING') && <AllResultList />}
          </S.Content>
          <S.TimerContainer>
            <Timer
              fontSize="12"
              size="72"
              time={timerTime}
              round={round}
              timeOverHandle={timeOverHandle}
            />
          </S.TimerContainer>
          <S.HistoryButton onClick={openHistoryModal}>
            <img src={images.awrsp.history} alt="기록" />
          </S.HistoryButton>
        </>
      ) : (
        // 게임 종료 -> 게임 결과 화면 렌더링
        <>
          <S.RoundDisplay>최종 결과</S.RoundDisplay>
          <S.Content>
            <S.GameResultList>
              <S.TableHeader>
                <p className="rank">등수</p>
                <p className="nickname">유저</p>
                <p className="point">획득 포인트</p>
              </S.TableHeader>
              {gameResult?.map((result) => (
                <S.GameResultItem key={result.nickname}>
                  <p className="rank">{result.rank}</p>
                  <p className="nickname">{result.nickname}</p>
                  <p className="point">{result.point}</p>
                </S.GameResultItem>
              ))}
            </S.GameResultList>
          </S.Content>
          <S.BackToLobbyButton
            onClick={() => {
              router.replace(`/awrsp/lobby`)
            }}
          >
            <img src={images.common.header.back} />
          </S.BackToLobbyButton>
        </>
      )}

      <Modal isOpen={isOpen}>
        <DrawCardModal closeModal={closeModal} />
      </Modal>

      <HModal isOpen={isHistoryOpen} closeModal={closeHistoryModal}>
        <HistoryModal closeModal={closeHistoryModal} />
      </HModal>
    </S.AwrspGameRoomContainer>
  )
}

export default withAuth(AwrspGameRoom)
