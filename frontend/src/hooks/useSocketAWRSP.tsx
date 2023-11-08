'use client'

import { useCallback, useRef } from 'react'
import { CompatClient, Stomp } from '@stomp/stompjs'
import { SOCKET_URL } from '@constants/baseUrl'
import SockJS from 'sockjs-client'
import { usePathname } from 'next/navigation'
import { socketResponseType } from '@type/common/common.type'
import { useRecoilValue, useSetRecoilState } from 'recoil'
import { userState } from '@atom/userAtom'
import { TimerState } from '@atom/awrspAtom'

const useSocketAWRSP = () => {
  const client = useRef<CompatClient>()
  const gameCode = usePathname().split('/')[3]
  const user = useRecoilValue(userState)
  const setTimerTime = useSetRecoilState(TimerState)

  /**
   * 전승 가위바위보 게임 구독
   */
  const connectAWRSPGame = useCallback(() => {
    console.log('전승 가위바위보 게임 구독', gameCode)

    client.current?.subscribe(`/exchange/game/awrsp.${gameCode}`, (res) => {
      const response: socketResponseType<unknown> = JSON.parse(res.body)
      console.log(response)

      if (response.type == 'CARD_SUBMIT') {
        const data = response.data as number
        setTimerTime(data)
      }
    })
  }, [client.current])

  /**
   * 전승 가위바위보 게임 구독 취소
   */
  const disconnectAWRSPGame = useCallback(() => {
    console.log('전승 가위바위보 게임 구독 취소', gameCode)

    client.current?.unsubscribe(`/exchange/game/awrsp.${gameCode}`)
  }, [client.current])

  /**
   * 게임 참가 (라운드마다 호출)
   */
  const handleRoundStart = useCallback(() => {
    console.log('라운드 시작!')

    client.current?.publish({
      destination: `/pub/game/awrsp.timer.${gameCode}`,
      body: JSON.stringify({
        nickname: user?.nickname,
        type: 'ENTER_GAME',
      }),
    })
  }, [client.current])

  const connectSocket = useCallback(() => {
    const sock = new SockJS(SOCKET_URL)
    const StompClient = Stomp.over(() => sock)

    client.current = StompClient

    client.current.connect(
      {},
      () => {
        connectAWRSPGame()
      },
      () => {
        disconnectAWRSPGame()
      },
    )
  }, [client.current])

  const disconnectSocket = useCallback(() => {
    client.current?.disconnect()
  }, [client.current])

  return {
    connectSocket,
    disconnectSocket,
  }
}

export default useSocketAWRSP
