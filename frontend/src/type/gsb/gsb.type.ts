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
  | 'Waiting'
  | 'Betting'
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
