'use client'

import { useCallback, useRef, useState } from 'react'
import { CompatClient, Stomp } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { SOCKET_URL } from '@constants/baseUrl'
import { usePathname } from 'next/navigation'
import { socketResponseType } from '@type/common/common.type'
import { useRecoilValue, useSetRecoilState } from 'recoil'
import { userState } from '@atom/userAtom'
import {
  AllBetChipsState,
  CurrentPlayerState,
  DisplayMessageState,
  MyState,
  OpponentState,
  RoundState,
  TimerState,
  TurnCardState,
} from '@atom/gsbAtom'
import {
  BettingResponseType,
  GsbSettingType,
  InitGameType,
  Player,
  PlayerInfoType,
  Turn,
  TurnListType,
} from '@type/gsb/gsb.type'

const useSocketGsb = () => {
  const client = useRef<CompatClient>()
  const gameCode = usePathname().split('/')[3]

  const user = useRecoilValue(userState)
  const setDisplayMessage = useSetRecoilState(DisplayMessageState)
  const setTurnList = useSetRecoilState(TurnCardState)
  const setCurrentPlayer = useSetRecoilState(CurrentPlayerState)
  const setMy = useSetRecoilState(MyState)
  const setOpponent = useSetRecoilState(OpponentState)
  const setAllBetChips = useSetRecoilState(AllBetChipsState)
  const setRound = useSetRecoilState(RoundState)
  const setTime = useSetRecoilState(TimerState)

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
        setRound('ChoiceTurn')
      }

      // 게임 초기값 받아오기
      else if (response.type === '게임정보') {
        const result = response as socketResponseType<InitGameType>
        setCurrentPlayer(result.data.currentPlayer)
        setAllBetChips(result.data.carryOverChips)
        const players = result.data.players as Player

        Object.keys(players).map((key) => {
          if (players[key as Turn].nickname == user?.nickname)
            setMy(players[key as Turn])
          else setOpponent(players[key as Turn])
        })

        if (user?.nickname === result.data.currentPlayer) {
          setRound('Combination')
          setDisplayMessage('금은동을 조합해서 올려주세요')
        } else {
          setRound('CombWaiting')
          setDisplayMessage('상대방이 금은동을 조합합니다')
        }
      }

      // 선공 금은동 세팅
      else if (response.type === '다음 플레이어 별 세팅') {
        const result = response as socketResponseType<GsbSettingType>
        if (result.data.currentPlayer === user?.nickname) {
          setMy((prev) => {
            if (!prev) return null
            return {
              ...prev,
              currentWeight: result.data.weight,
            }
          })
          // 선공 === 나 -> 후공이 셋팅하길 기다려야 함
          setRound('CombWaiting')
          setDisplayMessage('상대방이 금은동을 조합합니다')
        } else {
          setOpponent((prev) => {
            if (!prev) return null
            return {
              ...prev,
              currentWeight: result.data.weight,
            }
          })
          // 선공 === 상대방, 후공 === 나
          setRound('Combination')
          setDisplayMessage('금은동을 조합해서 올려주세요')
        }

        setCurrentPlayer(result.data.nextPlayer)
        setTime(result.data.timer)
      }
      // 두 플레이어의 금은동 세팅 종료 -> 베팅 시작
      else if (response.type === '다음 플레이어 베팅 시작') {
        const result = response as socketResponseType<GsbSettingType>
        if (result.data.currentPlayer === user?.nickname) {
          setMy((prev) => {
            if (!prev) return null
            return {
              ...prev,
              currentWeight: result.data.weight,
            }
          })
          setRound('BetWaiting')
          setDisplayMessage('상대방이 베팅 중입니다.')
        } else {
          setOpponent((prev) => {
            if (!prev) return null
            return {
              ...prev,
              currentWeight: result.data.weight,
            }
          })
          setRound('Betting')
          setDisplayMessage('베팅할 칩의 수를 입력해주세요.')
        }

        setCurrentPlayer(result.data.nextPlayer)
        setTime(result.data.timer)
      }

      // 다음 플레이어가 존재하면
      else if (response.type === '다음 플레이어 베팅') {
        const result = response as socketResponseType<BettingResponseType>
        if (result.data.currentPlayer === user?.nickname) {
          setMy((prev) => {
            if (!prev) return null
            return {
              ...prev,
              currentBetChips: prev.currentBetChips + result.data.currentChips,
            }
          })
          setRound('BetWaiting')
          setDisplayMessage('상대방이 베팅 중입니다.')
        } else {
          setOpponent((prev) => {
            if (!prev) return null
            return {
              ...prev,
              currentBetChips: prev.currentBetChips + result.data.currentChips,
            }
          })
          setRound('Betting')
          setDisplayMessage('베팅할 칩의 수를 입력해주세요.')
        }
        setAllBetChips(result.data.totalChips)
        setCurrentPlayer(result.data.nextPlayer)
        setTime(result.data.timer)
      }

      // 포기없이 베팅 종료
      else if (response.type === '라운드 종료') {
        const result = response as socketResponseType<BettingResponseType>
        if (result.data.currentPlayer === user?.nickname) {
          setMy((prev) => {
            if (!prev) return null
            return {
              ...prev,
              currentBetChips: prev.currentBetChips + result.data.currentChips,
            }
          })
        } else {
          setOpponent((prev) => {
            if (!prev) return null
            return {
              ...prev,
              currentBetChips: prev.currentBetChips + result.data.currentChips,
            }
          })
        }

        setAllBetChips(result.data.totalChips)
        setRound('Result')
        setDisplayMessage('금은동 조합을 공개합니다.')
      }

      // 라운드 결과(베팅 포기)
      else if (response.type === '베팅 포기 라운드 결과') {
        console.log('베팅 포기')
      }

      // 라운드 결과(승패가 있을 때)
      else if (response.type === '라운드 결과') {
        console.log('누군가는 이김')
      }

      // 라운드 결과(비김)
      else if (response.type === '라운드 결과 비김') {
        console.log('비겼어요!')
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
    },
    [client.current],
  )

  const disconnectSocket = useCallback(() => {
    client.current?.disconnect()
  }, [client.current])

  // 게임 참가
  const handleEnterGsb = useCallback(() => {
    console.log('금은동 게임 참가 : ', user?.nickname)

    setDisplayMessage('카드를 뒤집어 선공을 정해주세요')
    console.log()
    client.current?.publish({
      destination: `/pub/game.gsb.enter.${gameCode}`,
      body: JSON.stringify({
        nickname: user?.nickname,
      }),
    })
  }, [client.current])

  /**
   * 선후공 카드 선택
   * 선택한 카드의 인덱스를 전달
   * 카드 seq == 0이면 선공카드
   */
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

  /**
   *  금은동 세팅
   */
  const handleGSBComb = useCallback(
    (gold: number, silver: number, bronze: number) => {
      console.log('금은동 조합 전송')
      console.log('금: ', gold, ' 은: ', silver, ' 동: ', bronze)

      client.current?.publish({
        destination: `/pub/game.gsb.set-star.${gameCode}`,
        body: JSON.stringify({
          gold: gold,
          silver: silver,
          bronze: bronze,
        }),
      })
    },
    [client.current],
  )

  /**
   * 칩을 베팅
   */
  const handleBetting = useCallback(
    (giveUp: boolean, bettingChips: number) => {
      console.log('베팅: ', giveUp, bettingChips)

      client.current?.publish({
        destination: `/pub/game.gsb.betting.${gameCode}`,
        body: JSON.stringify({
          giveUp: giveUp,
          bettingChips: bettingChips,
        }),
      })
    },
    [client.current],
  )

  return {
    client,
    connectSocket,
    disconnectSocket,
    connectGsb,
    disconnectGsb,
    handleEnterGsb,
    handleChoiceTurnCard,
    handleGSBComb,
    handleBetting,
  }
}

export default useSocketGsb
