'use client'

import { useCallback, useRef, useState } from 'react'
import { CompatClient, Stomp } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { SOCKET_URL } from '@constants/baseUrl'
import { usePathname } from 'next/navigation'
import { socketResponseType } from '@type/common/common.type'
import { useRecoilValue, useSetRecoilState } from 'recoil'
import { userState } from '@atom/userAtom'
import { DisplayMessageState, TurnCardState } from '@atom/gsbAtom'
import { TurnListType } from '@type/gsb/gsb.type'

const useSocketGsb = () => {
  const [sockjs, setSockjs] = useState<WebSocket>()
  const client = useRef<CompatClient>()
  const gameCode = usePathname().split('/')[3]

  const user = useRecoilValue(userState)
  const setDisplayMessage = useSetRecoilState(DisplayMessageState)
  const setTurnList = useSetRecoilState(TurnCardState)

  // 금은동 게임 구독
  const connectGsb = useCallback(() => {
    console.log('금은동 게임 구독 : ', client, gameCode, user)
    console.log(client.current)

    client.current?.subscribe(`/exchange/game/gsb.${gameCode}`, (res) => {
      const response: socketResponseType<unknown> = JSON.parse(res.body)
      console.log(response)

      if (response.type === '플레이어순서') {
        const result = response as socketResponseType<TurnListType[]>
        setTurnList(result.data)
      }
    })
  }, [client.current, gameCode])

  const disconnectGsb = useCallback(() => {
    console.log('금은동 게임 구독 취소')

    client.current?.unsubscribe(`/exchange/game/gsb.${gameCode}`)
  }, [client.current, gameCode])

  // 소켓 연결
  const connectSocket = useCallback(
    (connectedFunction: () => void, disconnectedFunction: () => void) => {
      const sock = new SockJS(SOCKET_URL)
      const StompClient = Stomp.over(() => sock)
      console.log(sock)
      sock.onmessage = (e) => {
        console.log('맹맹', e.data)
      }
      client.current = StompClient

      // StompClient.debug = () => {}

      // 연결 되면
      client.current.connect(
        {},
        () => {
          connectedFunction()
          handleEnterGsb()
        },
        // 연결 종료 시
        () => {
          disconnectedFunction()
          // 게임 나가기
        },
      )

      setSockjs(sock)
    },
    [client.current],
  )

  const disconnectSocket = useCallback(() => {
    client.current?.disconnect()
  }, [client.current])

  // 게임 참가
  const handleEnterGsb = useCallback(() => {
    console.log('금은동 게임 참가 : ', user?.nickname)

    setDisplayMessage('카드를 뒤집어 선공을 정해주세요.')
    console.log()
    client.current?.publish({
      destination: `/pub/game.gsb.enter.${gameCode}`,
      body: JSON.stringify({
        nickname: user?.nickname,
      }),
    })
  }, [client.current])

  // 선후공 카드 선택
  // 선택한 카드의 인덱스를 전달
  // 카드 seq == 0이면 선공카드
  const handleChoiceTurnCard = useCallback(
    (index: number) => {
      console.log('선후공 카드 선택 : ', index)

      client.current?.publish({
        destination: `/pub/game.gsb.set-player.${gameCode}`,
        body: JSON.stringify({
          nickname: user?.nickname,
          seq: index,
        }),
      })
    },
    [client.current],
  )

  return {
    client,
    sockjs,
    connectSocket,
    disconnectSocket,
    connectGsb,
    disconnectGsb,
    handleEnterGsb,
    handleChoiceTurnCard,
  }
}

export default useSocketGsb
