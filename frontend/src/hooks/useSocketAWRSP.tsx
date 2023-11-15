'use client'

import { useRef, useState } from 'react'
import { CompatClient, Stomp } from '@stomp/stompjs'
import { SOCKET_URL } from '@constants/baseUrl'
import SockJS from 'sockjs-client'
import { usePathname } from 'next/navigation'
import { socketResponseType } from '@type/common/common.type'
import { useRecoilState, useRecoilValue, useSetRecoilState } from 'recoil'
import { userState } from '@atom/userAtom'
import {
  DrawCardState,
  GameResultState,
  PlayerResultState,
  RoundState,
  TimerState,
} from '@atom/awrspAtom'
import {
  GameResultType,
  PlayerResultType,
  RspType,
  StepType,
} from '@type/awrsp/awrsp.type'

const useSocketAWRSP = () => {
  const client = useRef<CompatClient>()
  const gameCode = usePathname().split('/')[3]
  const user = useRecoilValue(userState)
  const setTimerTime = useSetRecoilState(TimerState)
  const setPlayerResult = useSetRecoilState(PlayerResultState)
  const setGameReulst = useSetRecoilState(GameResultState)
  const setRound = useSetRecoilState(RoundState)
  const [drawCard, setDrawCard] = useRecoilState(DrawCardState)

  const [step, setStep] = useState<StepType>('ENTER_GAME')

  /**
   * 전승 가위바위보 게임 구독
   */
  const connectAWRSPGame = () => {
    console.log('전승 가위바위보 게임 구독', gameCode)

    client.current?.subscribe(`/exchange/game/awrsp.${gameCode}`, (res) => {
      const response: socketResponseType<unknown> = JSON.parse(res.body)
      console.log(response)

      // 카드 조합 제한 시간을 받아서 타이머 시간 설정
      if (
        response.type === 'CARD_SUBMIT' ||
        response.type === 'DRAW_CARD' ||
        response.type === 'PLAYER_WINS' ||
        response.type === 'ALL_WINS'
      ) {
        console.log(step, '이후 타이머 시간 설정')
        if (step === 'WAITING') return
        const data = response.data as number
        console.log('받아온 시간 : ', data)
        setTimerTime(data)
        setStep(response.type as StepType)

        // 비김 카드 선택하면 비김 카드 셋팅할 준비
        if (response.type === 'DRAW_CARD') {
          setDrawCard({ ...drawCard, isSetting: false })
        }
      }
      // 몇 라운드인지 받아오기
      else if (response.type === 'ROUND') {
        console.log('라운드를 받아올 때: ', step)

        if (step === 'WAITING') return
        const data = response.data as number
        console.log(data, '라운드')
        setRound(data)
      }
      // 모든 유저의 라운드 결과를 받아오기
      else if (response.type === 'CARD_RESULT') {
        console.log('라운드 결과를 받아올 때: ', step)

        if (step === 'WAITING') return
        const data = response.data as PlayerResultType[]
        console.log('이번 라운드 결과 : ', data)
        setPlayerResult(data)
      }
      // 게임 종료, 순위 받아오기
      else if (response.type === 'GAME_OVER') {
        const data = response.data as GameResultType[]
        console.log('게임 결과 : ', data)
        setGameReulst(data)
        setStep(response.type as StepType)
      }
    })
  }

  /**
   * 전승 가위바위보 게임 구독 취소
   */
  const disconnectAWRSPGame = () => {
    console.log('전승 가위바위보 게임 구독 취소', gameCode)

    client.current?.unsubscribe(`/exchange/game/awrsp.${gameCode}`)
  }

  /**
   * 게임 참가 (라운드마다 호출)
   */
  const handleRoundStart = () => {
    client.current?.publish({
      destination: `/pub/game.awrsp.timer.${gameCode}`,
      body: JSON.stringify({
        nickname: user?.nickname,
        type: 'ENTER_GAME',
      }),
    })
  }

  /**
   * 타이머가 종료되었을 때 호출
   */
  const handleTimeOver = (step: StepType) => {
    console.log('종료되는 단계 : ', step)
    if (step === 'ALL_WINS') {
      // 다음 라운드 시작을 알림
      setStep('ENTER_GAME')
      setTimerTime(20)
      handleRoundStart()
    } else if (step === 'WAITING') return
    else {
      client.current?.publish({
        destination: `/pub/game.awrsp.timer.${gameCode}`,
        body: JSON.stringify({
          nickname: user?.nickname,
          type: step,
        }),
      })
    }
  }
  /**
   * 카드 제출
   */
  const handleCardSubmit = (combCard: RspType[]) => {
    console.log('카드 조합 : ', combCard)
    client.current?.publish({
      destination: `/pub/game.awrsp.submit.${gameCode}`,
      body: JSON.stringify({
        nickname: user?.nickname,
        card: combCard,
      }),
    })
  }

  const connectSocket = () => {
    const sock = new SockJS(SOCKET_URL)
    const StompClient = Stomp.over(() => sock)

    client.current = StompClient

    client.current.connect(
      {
        nickname: user?.nickname,
      },
      () => {
        connectAWRSPGame()
        handleRoundStart()
      },
      () => {
        disconnectAWRSPGame()
      },
    )
  }

  const disconnectSocket = () => {
    client.current?.disconnect()
  }

  return {
    connectSocket,
    disconnectSocket,
    handleRoundStart,
    handleTimeOver,
    handleCardSubmit,
    step,
    setStep,
  }
}

export default useSocketAWRSP
