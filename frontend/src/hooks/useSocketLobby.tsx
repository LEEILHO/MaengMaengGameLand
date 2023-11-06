'use client'

import { SOCKET_URL } from '@constants/baseUrl'
import { useCallback, useState, useRef, useEffect } from 'react'
import SockJS from 'sockjs-client'
import { CompatClient, Stomp } from '@stomp/stompjs'
import { useRecoilValue, useSetRecoilState } from 'recoil'
import { channelState, gameTypeState } from '@atom/gameAtom'
import { roomsState } from '@atom/lobbyAtom'
import { socketResponseType } from '@type/common/common.type'
import { RoomType } from '@type/lobby/lobby.type'

const useSocket = () => {
  const [sockjs, setSockjs] = useState<WebSocket>()
  const client = useRef<CompatClient>()
  const gameType = useRecoilValue(gameTypeState)
  const channel = useRecoilValue(channelState)
  const setRooms = useSetRecoilState(roomsState)

  const connectLobby = useCallback(() => {
    console.log('로비 구독', client, gameType, channel)
    console.log(client.current)
    // 로비 구독
    client.current?.subscribe(
      `/exchange/room/lobby.${gameType}.${channel}`,
      (res) => {
        const result = JSON.parse(res.body) as socketResponseType<RoomType[]>
        // console.log(JSON.parse(res.body), '멩멩')
        setRooms(result.data)
      },
    )
  }, [client.current, gameType, channel])

  const disconnectLobby = useCallback(() => {
    client.current?.unsubscribe(`/exchange/room/lobby.${gameType}.${channel}`)
  }, [client.current, gameType, channel])

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
        },
        // 연결 종료 시
        () => {
          disconnectedFunction()
        },
      )

      setSockjs(sock)
    },
    [client.current],
  )

  const disconnectSocket = useCallback(() => {
    client.current?.disconnect()
  }, [client.current])

  return {
    client,
    sockjs,
    connectSocket,
    disconnectSocket,
    connectLobby,
    disconnectLobby,
  }
}

export default useSocket
