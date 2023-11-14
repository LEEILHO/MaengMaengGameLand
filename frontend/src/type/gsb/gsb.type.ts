import { TireType } from '@type/common/common.type'

export type TurnListType = {
  seq: number
  nickname: string | null
  selected: boolean
}

export type PlayerInfoType = {
  nickname: string
  profileUrl: string
  tier: TireType
  currentChips: number
  currentGold: number
  currentSilver: number
  currentBronze: number
  currentWeight: number
  histories: null
}

export type ParticipiantType = {
  nickname: string
  ready: boolean
  host: boolean
  profileUrl: string
  tier: TireType
}

export type Turn = '0' | '1'

export type Player = {
  [key in Turn]: PlayerInfoType
}

export type InitGameType = {
  gameCode: string
  roomCode: string
  currentRound: number
  carryOverChips: number
  currentPlayer: string
  startCards: TurnListType[]
  players: Player
  participiants: ParticipiantType[]
}

export type RoundType =
  | 'ChoiceTurn'
  | 'Combination'
  | 'CombWaiting'
  | 'Betting'
  | 'BetWaiting'
  | 'Result'
  | 'DrawResult'
  | 'GiveUpResult'
  | 'GameOver'

export type StarStatus = 'in' | 'out'

export type StarType = {
  id: string
  status: StarStatus
  src: string
}

export type StarListType = {
  [key in StarStatus]: StarType[]
}

export type GsbSettingType = {
  currentPlayer: string
  weight: number
  currentChips: number
  defaultChips: number
  nextPlayer: string
  timer: number
}

export type BettingResponseType = {
  nextPlayer: string | null
  currentPlayer: string
  currentChips: number
  currentPlayerChips: number // 현재 플레이어의 보유 칩
  totalChips: number
  carryOverChips: number
  timer: number
}

// 일반적인 결과
export type NormalResultType = {
  currentRound: number
  nextRound: number
  nextPlayer: string
  timer: number
  draw: boolean
  winner: string
  winnerGold: number
  winnerSilver: number
  winnerBronze: number
  currentWinnerChips: number
  loser: string
  loserGold: number
  loserSilver: number
  loserBronze: number
  currentLoserChips: number
}

// 비겼을 때 결과
export type DrawResultType = {
  currentRound: number
  nextRound: number
  nextPlayer: string
  timer: number
  draw: boolean
  player1: string
  currentPlayer1Chips: number
  player2: string
  currentPlayer2Chips: number
}

// 누군가 베팅 포기했을 때 결과
export type GiveUpResultType = {
  currentRound: number
  nextRound: number
  nextPlayer: string
  timer: number
  winner: string
  currentWinnerChips: number
  loser: string
  currentLoserChips: number
}

// 게임 종료, 최종 결과 타입
export type GameOverType = {
  draw: boolean
  winner: string
  winnerChips: number
  loser: string
  loserChips: number
}

export type CombResultType = {
  gold: number
  silver: number
  bronze: number
}
