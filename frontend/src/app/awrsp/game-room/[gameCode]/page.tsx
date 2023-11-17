'use client'

import * as S from '@styles/awrsp/AwrspGameRoom.styled'
import withAuth from '@components/hoc/client/PrivateRoute'
import { images } from '@constants/images'
import Timer from '@components/common/clients/Timer'
import RspCombination from '@components/awrsp/client/RspCombination'
import useModal from '@hooks/useModal'
import DrawCardModal from '@components/awrsp/client/DrawCardModal'
import { useEffect, useCallback } from 'react'
import MyResult from '@components/awrsp/client/MyResult'
import AllResultList from '@components/awrsp/client/AllResultList'
import { useRecoilValue, useResetRecoilState } from 'recoil'
import {
  DrawCardState,
  GameResultState,
  HistoryState,
  PlayerResultState,
  RoundState,
  RspCardListState,
  TimerState,
} from '@atom/awrspAtom'
import useSocketAWRSP from '@hooks/useSocketAWRSP'
import useDidMountEffect from '@hooks/useDidMoundEffect'
import HistoryModal from '@components/awrsp/client/HistoryModal'
import { useRouter } from 'next/navigation'
import useSound from '@hooks/useSound'

const AwrspGameRoom = () => {
  const router = useRouter()
  const {
    connectSocket,
    disconnectSocket,
    handleTimeOver,
    handleCardSubmit,
    step,
    setStep,
  } = useSocketAWRSP()
  const { Modal, closeModal, isOpen, openModal } = useModal()
  const {
    Modal: HModal,
    closeModal: closeHistoryModal,
    isOpen: isHistoryOpen,
    openModal: openHistoryModal,
  } = useModal()
  const timerTime = useRecoilValue(TimerState)
  const round = useRecoilValue(RoundState)
  const gameResult = useRecoilValue(GameResultState)

  // 끝나고 리셋을 위한 함수
  const resetCardList = useResetRecoilState(RspCardListState)
  const resetDrawCard = useResetRecoilState(DrawCardState)
  const resetTimerReset = useResetRecoilState(TimerState)
  const resetPlayerResult = useResetRecoilState(PlayerResultState)
  const resetGameResult = useResetRecoilState(GameResultState)
  const resetHistory = useResetRecoilState(HistoryState)

  // 효과음
  const { playButtonSound } = useSound()

  const timeOverHandle = () => {
    if (step) {
      console.log(step, '종료')
      // 정답을 맞추지 않았다면 다음 단계로 넘어가기 위해 타이머 종료 신호 전송
      if (step !== 'WAITING') {
        handleTimeOver(step)
      } else return
    }
  }

  const onClickBackToLobby = () => {
    playButtonSound()
    resetCardList()
    resetDrawCard()
    resetTimerReset()
    resetPlayerResult()
    resetGameResult()
    resetHistory()
    router.replace(`/awrsp/lobby`)
  }

  const onClickHistory = useCallback(() => {
    playButtonSound()
    openHistoryModal()
  }, [])

  useEffect(() => {
    connectSocket()

    return () => {
      disconnectSocket()
    }
  }, [])

  useEffect(() => {
    console.log('스텝변경: ', step)

    if (step === 'DRAW_CARD') openModal()
    openModal()
  }, [step])

  return (
    <S.AwrspGameRoomContainer>
      {step !== 'GAME_OVER' ? (
        // 게임이 종료되지 않았다면
        <>
          <S.RoundDisplay>{round} Round</S.RoundDisplay>
          <S.Content>
            {(step === 'CARD_SUBMIT' || step === 'DRAW_CARD') && (
              <RspCombination handleCardSubmit={handleCardSubmit} />
            )}
            <RspCombination handleCardSubmit={handleCardSubmit} />
            {step === 'PLAYER_WINS' && <MyResult />}
            {(step === 'ALL_WINS' || step === 'WAITING') && (
              <AllResultList setStep={setStep} />
            )}
          </S.Content>
          {step !== 'WAITING' && (
            <S.TimerContainer>
              <Timer
                fontSize="12"
                size="72"
                time={timerTime}
                round={round}
                timeOverHandle={timeOverHandle}
              />
            </S.TimerContainer>
          )}
          <S.HistoryButton onClick={onClickHistory}>
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
                <p className="point">맞춘 라운드</p>
              </S.TableHeader>
              {gameResult?.map((result) => (
                <S.GameResultItem key={result.nickname}>
                  <p className="rank">{result.rank}</p>
                  <p className="nickname">{result.nickname}</p>
                  <p className="point">
                    {result.round !== 0 ? result.round : '-'}
                  </p>
                </S.GameResultItem>
              ))}
            </S.GameResultList>
          </S.Content>
        </>
      )}

      {(step === 'GAME_OVER' || step === 'WAITING') && (
        <S.BackToLobbyButton onClick={onClickBackToLobby}>
          <img src={images.common.header.back} />
        </S.BackToLobbyButton>
      )}

      <Modal isOpen={isOpen}>
        <DrawCardModal closeModal={closeModal} step={step} />
      </Modal>

      <HModal isOpen={isHistoryOpen} closeModal={closeHistoryModal}>
        <HistoryModal closeModal={closeHistoryModal} />
      </HModal>
    </S.AwrspGameRoomContainer>
  )
}

export default withAuth(AwrspGameRoom)
