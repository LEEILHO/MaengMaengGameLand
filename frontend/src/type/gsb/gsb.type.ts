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
