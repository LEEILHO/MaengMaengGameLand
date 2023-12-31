'use client'

import { useRef, useState } from 'react'
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
  GameOverState,
  MyBetChipsState,
  MyState,
  OpponentBetChipsState,
  OpponentState,
  ResultState,
  RoundState,
  TimerState,
  TurnCardState,
} from '@atom/gsbAtom'
import {
  BettingResponseType,
  DrawResultType,
  GameOverType,
  GiveUpResultType,
  GsbSettingType,
  InitGameType,
  NormalResultType,
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
  const setMyBetChips = useSetRecoilState(MyBetChipsState)
  const setOpponent = useSetRecoilState(OpponentState)
  const setOpponentBetChips = useSetRecoilState(OpponentBetChipsState)
  const setAllBetChips = useSetRecoilState(AllBetChipsState)
  const setResult = useSetRecoilState(ResultState)
  const setRound = useSetRecoilState(RoundState)
  const setTime = useSetRecoilState(TimerState)
  const setGameOver = useSetRecoilState(GameOverState)

  // 금은동 게임 구독
  const connectGsb = () => {
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
              currentChips: result.data.currentChips,
            }
          })
          setMyBetChips(result.data.defaultChips)
          // 선공 === 나 -> 후공이 셋팅하길 기다려야 함
          setRound('CombWaiting')
          setDisplayMessage('상대방이 금은동을 조합합니다')
        } else {
          setOpponent((prev) => {
            if (!prev) return null
            return {
              ...prev,
              currentWeight: result.data.weight,
              currentChips: result.data.currentChips,
            }
          })
          setOpponentBetChips(result.data.defaultChips)
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
              currentChips: result.data.currentChips,
            }
          })
          setMyBetChips((prev) => {
            return result.data.defaultChips + prev
          })
          setRound('BetWaiting')
          setDisplayMessage('상대방이 베팅 중입니다.')
        } else {
          setOpponent((prev) => {
            if (!prev) return null
            return {
              ...prev,
              currentWeight: result.data.weight,
              currentChips: result.data.currentChips,
            }
          })
          setOpponentBetChips(result.data.defaultChips)
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
          setMyBetChips(result.data.totalChips)
          setMy((prev) => {
            if (!prev) return null
            return { ...prev, currentChips: result.data.currentPlayerChips }
          })
          setRound('BetWaiting')
          setDisplayMessage('상대방이 베팅 중입니다.')
        } else {
          setOpponentBetChips(result.data.totalChips)
          setOpponent((prev) => {
            if (!prev) return null
            return { ...prev, currentChips: result.data.currentPlayerChips }
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
          setMyBetChips((prev) => prev + result.data.currentChips)
          setMy((prev) => {
            if (!prev) return null
            return { ...prev, currentChips: result.data.currentPlayerChips }
          })
        } else {
          setOpponentBetChips((prev) => prev + result.data.currentChips)
          setOpponent((prev) => {
            if (!prev) return null
            return { ...prev, currentChips: result.data.currentPlayerChips }
          })
        }

        setAllBetChips(result.data.carryOverChips)
      }

      // 라운드 결과(베팅 포기)
      else if (response.type === '베팅 포기 라운드 결과') {
        console.log('베팅 포기')
        const result = response as socketResponseType<GiveUpResultType>
        setRound('GiveUpResult')
        setDisplayMessage(`${result.data.loser}님이 포기하셨습니다.`)
        setCurrentPlayer(result.data.nextPlayer)
        setTime(result.data.timer)
        if (user?.nickname === result.data.winner) {
          setMy((prev) => {
            if (!prev) return null
            return { ...prev, currentChips: result.data.currentWinnerChips }
          })
          setOpponent((prev) => {
            if (!prev) return null
            return { ...prev, currentChips: result.data.currentLoserChips }
          })
        } else {
          setMy((prev) => {
            if (!prev) return null
            return { ...prev, currentChips: result.data.currentLoserChips }
          })
          setOpponent((prev) => {
            if (!prev) return null
            return { ...prev, currentChips: result.data.currentWinnerChips }
          })
        }

        // 현재까지 베팅한 칩 초기화
        setMyBetChips(0)
        setOpponentBetChips(0)
      }

      // 라운드 결과(승패가 있을 때)
      else if (response.type === '라운드 결과') {
        console.log('누군가는 이김')
        const result = response as socketResponseType<NormalResultType>
        setResult(result.data)
        setRound('Result')
        setDisplayMessage('금은동 조합을 공개합니다.')
        setCurrentPlayer(result.data.nextPlayer)
        setTime(result.data.timer)

        // 현재까지 베팅한 칩 초기화
        setMyBetChips(0)
        setOpponentBetChips(0)
      }

      // 라운드 결과(비김)
      else if (response.type === '라운드 결과 비김') {
        console.log('비겼어요!')
        const result = response as socketResponseType<DrawResultType>
        setRound('DrawResult')
        setDisplayMessage('비겼습니다.')

        setCurrentPlayer(result.data.nextPlayer)
        setTime(result.data.timer)
      }

      // 게임 종료
      else if (response.type === '게임 결과') {
        console.log('게임 종료!')
        const result = response as socketResponseType<GameOverType>
        setGameOver(result.data)
      }
    })
  }

  const disconnectGsb = () => {
    console.log('금은동 게임 구독 취소')

    client.current?.unsubscribe(`/exchange/game/gsb.${gameCode}`)
  }

  // 소켓 연결
  const connectSocket = (
    connectedFunction: () => void,
    disconnectedFunction: () => void,
  ) => {
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
      {
        nickname: user?.nickname,
      },

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
  }

  const disconnectSocket = () => {
    client.current?.disconnect()
  }

  // 게임 참가
  const handleEnterGsb = () => {
    console.log('금은동 게임 참가 : ', user?.nickname)

    setDisplayMessage('카드를 뒤집어 선공을 정해주세요')
    console.log()
    client.current?.publish({
      destination: `/pub/game.gsb.enter.${gameCode}`,
      body: JSON.stringify({
        nickname: user?.nickname,
      }),
    })
  }

  /**
   * 선후공 카드 선택
   * 선택한 카드의 인덱스를 전달
   * 카드 seq == 0이면 선공카드
   */
  const handleChoiceTurnCard = (index: number) => {
    console.log('선후공 카드 선택 : ', index)

    client.current?.publish({
      destination: `/pub/game.gsb.set-player.${gameCode}`,
      body: JSON.stringify({
        nickname: user?.nickname,
        seq: index,
      }),
    })
  }

  /**
   *  금은동 세팅
   */
  const handleGSBComb = (gold: number, silver: number, bronze: number) => {
    console.log('금은동 조합 전송')
    console.log('금: ', gold, ' 은: ', silver, ' 동: ', bronze)

    setMy((prev) => {
      if (!prev) return null
      return {
        ...prev,
        currentGold: prev.currentGold - gold,
        currentSilver: prev.currentSilver - silver,
        currentBronze: prev.currentBronze - bronze,
      }
    })

    client.current?.publish({
      destination: `/pub/game.gsb.set-star.${gameCode}`,
      body: JSON.stringify({
        gold: gold,
        silver: silver,
        bronze: bronze,
      }),
    })
  }

  /**
   * 칩을 베팅
   */
  const handleBetting = (giveUp: boolean, bettingChips: number) => {
    console.log('베팅: ', giveUp, bettingChips)

    client.current?.publish({
      destination: `/pub/game.gsb.betting.${gameCode}`,
      body: JSON.stringify({
        giveUp: giveUp,
        bettingChips: bettingChips,
      }),
    })
  }

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
