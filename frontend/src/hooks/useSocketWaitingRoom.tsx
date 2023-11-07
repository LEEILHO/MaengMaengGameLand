'use client'

import { useCallback, useState, useRef } from 'react'
import { useRecoilValue, useSetRecoilState } from 'recoil'
import { RoomInfoState } from '@atom/waitingRoomAtom'
import { socketResponseType } from '@type/common/common.type'
import {
  ChatMessageType,
  RoomInfoType,
} from '@type/waitingRoom/waitingRoom.type'
import { CompatClient, Stomp } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { SOCKET_URL } from '@constants/baseUrl'
import { userState } from '@atom/userAtom'
import { usePathname, useRouter } from 'next/navigation'
import { ChatListState } from '@atom/chatAtom'

const useSocketWaitingRoom = () => {
  const router = useRouter()
  const roomCode = usePathname().split('/')[3]
  const gameType = usePathname().split('/')[1]
  const [sockjs, setSockjs] = useState<WebSocket>()
  const client = useRef<CompatClient>()
  const setRoomInfo = useSetRecoilState(RoomInfoState)
  const user = useRecoilValue(userState)
  const setChatList = useSetRecoilState(ChatListState)

  const connectWaitingRoom = useCallback(() => {
    console.log('대기방 구독', client, roomCode)
    console.log(client.current)
    client.current?.subscribe(`/exchange/room/room.${roomCode}`, (res) => {
      const response: socketResponseType<unknown> = JSON.parse(res.body)
      console.log(response)
      if (response.type === 'ROOM_INFO') {
        const result = response as socketResponseType<RoomInfoType>
        console.log('방 정보: ', result.data)
        setRoomInfo(result.data)
      } else if (response.type === 'ROOM_CHAT') {
        const result = response as socketResponseType<ChatMessageType>
        console.log('채팅 : ', result.data)
        setChatList((prevList) => [...prevList, result.data])
      } else if (response.type == 'GAME_START') {
        const result = response as socketResponseType<string>
        console.log('게임방 코드 : ', result.data)
        router.replace(`/${gameType}/game-room/${result.data}`)
      }
    })
  }, [client.current, roomCode])

  const disconnectWaitingRoom = useCallback(() => {
    console.log('대기방 구독 취소', client, roomCode)
    client.current?.unsubscribe(`/exchange/room/room.${roomCode}`)
  }, [client.current, roomCode])

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
          handleEnter()
        },
        // 연결 종료 시
        () => {
          disconnectedFunction()
          handleExit()
        },
      )

      setSockjs(sock)
    },
    [client.current],
  )

  const disconnectSocket = useCallback(() => {
    client.current?.disconnect()
  }, [client.current])

  const handleEnter = useCallback(() => {
    console.log('대기방 입장: ', roomCode)
    client.current?.publish({
      destination: `/pub/room.enter.${roomCode}`,
      body: JSON.stringify({
        roomCode: roomCode,
        nickname: user?.nickname,
      }),
    })
  }, [client.current, roomCode])

  // 자리 열고 닫기
  const handleEmptySeat = useCallback(
    (index: number) => {
      client.current?.publish({
        destination: `/pub/room.seat.${roomCode}`,
        body: JSON.stringify({
          nickname: user?.nickname,
          seatNumber: index,
        }),
      })
    },
    [client.current],
  )

  // 대기방 나가기
  const handleExit = useCallback(() => {
    // 채팅 리코일 초기화
    setChatList([])

    client.current?.publish({
      destination: `/pub/room.exit.${roomCode}`,
      body: JSON.stringify({
        nickname: user?.nickname,
      }),
    })
  }, [client.current])

  // 채팅 보내기
  const handleSendChat = useCallback(
    (message: string) => {
      console.log('보낼 메세지 : ', user?.nickname, message)
      client.current?.publish({
        destination: `/pub/room.chat.${roomCode}`,
        body: JSON.stringify({
          nickname: user?.nickname,
          message: message,
        }),
      })
    },
    [client.current],
  )

  // 레디 하기
  const handleReady = useCallback(() => {
    client.current?.publish({
      destination: `/pub/room.ready.${roomCode}`,
      body: JSON.stringify({
        nickname: user?.nickname,
      }),
    })
  }, [client.current])

  // 게임 시작
  const handleGameStart = useCallback(() => {
    console.log('게임시작')
    client.current?.publish({
      destination: `/pub/room.start.${roomCode}`,
      body: JSON.stringify({
        nickname: user?.nickname,
      }),
    })
  }, [client.current])

  // 대기방 설정 변경
  const handleUpdateRoom = useCallback(
    (newTitle: string, publicRoom: boolean) => {
      console.log('대기방 설정 변경 : ', newTitle, publicRoom)
      client.current?.publish({
        destination: `/pub/room.setting.${roomCode}`,
        body: JSON.stringify({
          nickname: user?.nickname,
          title: newTitle,
          publicRoom: publicRoom,
        }),
      })
    },
    [client.current],
  )

  // 강퇴
  const handleKick = useCallback(
    (kickPlayer: string) => {
      console.log('강퇴 : ', kickPlayer)
      client.current?.publish({
        destination: `/pub/room.kick.${roomCode}`,
        body: JSON.stringify({
          nickname: user?.nickname,
          kickPlayer: kickPlayer,
        }),
      })
    },
    [client.current],
  )

  return {
    connectSocket,
    disconnectSocket,
    connectWaitingRoom,
    disconnectWaitingRoom,
    handleEmptySeat,
    handleExit,
    handleSendChat,
    handleReady,
    handleUpdateRoom,
    handleGameStart,
    handleKick,
  }
}

export default useSocketWaitingRoom
