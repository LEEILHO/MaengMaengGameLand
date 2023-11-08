'use client'

import {
  jwacJewelryItemState,
  jwacPlayerListState,
  jwacRoundResultState,
  jwacRoundState,
} from '@atom/jwacAtom'
import { SOCKET_URL } from '@constants/baseUrl'
import { CompatClient, Stomp } from '@stomp/stompjs'
import { socketResponseType } from '@type/common/common.type'
import {
  GameInfoType,
  RoundDataType,
  RoundResultType,
  SpecialItemResultType,
} from '@type/gameRoom/jwac.type'
import { useCallback, useState, useRef } from 'react'
import { useRecoilState, useSetRecoilState } from 'recoil'
import SockJS from 'sockjs-client'

const useSocketJWAC = () => {
  const client = useRef<CompatClient>()
  const [playerList, setPlayerList] = useRecoilState(jwacPlayerListState)
  const [roundData, setRoundData] = useRecoilState(jwacRoundState)
  const setRoundTotalData = useSetRecoilState(jwacRoundResultState)
  const setJewelryItem = useSetRecoilState(jwacJewelryItemState)

  /**
   * 게임 시작 시 서버로 게임 시작을 알린다.
   */
  const gameStart = (code: string, nickname: string) => {
    client.current?.publish({
      destination: `/pub/game.jwac.enter.${code}`,
      body: JSON.stringify({
        gameCode: code,
        nickname: nickname,
      }),
    })
  }

  /**
   * 낙찰 가격을 제출한다.
   */
  const handleBid = (code: string, nickname: string, money: number) => {
    console.log('현재 라운드', roundData)

    client.current?.publish({
      destination: `/pub/game.jwac.bid.${code}`,
      body: JSON.stringify({
        roomCode: code,
        nickname: nickname,
        round: roundData?.round,
        bidAmount: money,
      }),
    })
  }

  /**
   * 시간이 종료되면 서버에게 아려준다.
   */
  const timeOver = (code: string, nickname: string) => {
    client.current?.publish({
      destination: `/pub/game.jwac.time.${code}`,
      body: JSON.stringify({
        roomCode: code,
        nickname: nickname,
      }),
    })
  }

  /**
   * 게임을 구독한다.
   */
  const connectGame = (code: string) => {
    client.current?.subscribe(`/exchange/game/jwac.${code}`, (res) => {
      console.log(JSON.parse(res.body))

      // 게임 관련 메시지 처리
      const result = JSON.parse(res.body) as socketResponseType<unknown>

      // 유저 초기 세팅
      if (result.type === 'GAME_INFO') {
        const data = result.data as GameInfoType
        const initPlayers = data.playerInfo.map((player) => {
          return {
            score: 0,
            item: false,
            bidSum: 0,
            ...player,
          }
        })
        setPlayerList(initPlayers)
      }

      // 게임 라운드 시작
      if (result.type === 'GAME_ROUND_START') {
        const data = result.data as RoundDataType
        console.log('게임 라운드 스타트', data)

        setRoundData(data)
      }

      // 게임 결과 받아서 업데이트
      if (result.type === 'GAME_ROUND_RESULT') {
        const data = result.data as RoundResultType
        const newPlayerList = playerList.map((player) => {
          return {
            ...player,
            ...data.players.filter((item) => item.nickname === player.nickname),
          }
        })
        setRoundTotalData(data)
        console.log('[업데이트 된 플레이어 리스트]', newPlayerList)
        setPlayerList(newPlayerList)
      }

      // 보석 정보 확인권 아이템 정보 받기
      if (result.type === 'GAME_SPECIAL_ITEM_RESULT') {
        const data = result.data as SpecialItemResultType
        setJewelryItem(data.itemResult)
      }
    })
  }

  const connectSocket = useCallback((code: string, nickname: string) => {
    const sock = new SockJS(SOCKET_URL)
    const StompClient = Stomp.over(() => sock)

    client.current = StompClient

    client.current.connect(
      {},
      () => {
        connectGame(code)
        gameStart(code, nickname)
      },
      () => {},
    )
  }, [])

  return {
    connectSocket,
    handleBid,
    timeOver,
  }
}

export default useSocketJWAC
