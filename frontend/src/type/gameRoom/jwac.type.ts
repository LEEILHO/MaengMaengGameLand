import { TireType } from '@type/common/common.type'

export interface GameInfoType {
  gameCode: string
  playerInfo: PlayerType[]
}

export interface PlayerType {
  score: number
  item: boolean
  bidSum: number
  nickname: string
  profileUrl: string
  tier: TireType
}

export type JewelryType =
  | 'DIAMOND'
  | 'RUBY'
  | 'SAPPHIRE'
  | 'EMERALD'
  | 'SPECIAL'

export interface RoundDataType {
  gameCode: string
  round: number
  time: number
  jewelry: JewelryType
  jewelryScore: number
}

export interface RoundResultType {
  gameCode: string
  mostBidder: string
  leastBidder: string
  roundBidSum: number
  round: number
  jewelryScore: number
  panaltyScore: number
  players: PlayerType[]
}

export type JewelryItemType = {
  EMERALD: number
  DIAMOND: number
  SAPPHIRE: number
  RUBY: number
}

export type SpecialItemResultType = {
  itemResult: JewelryItemType
  nickname: string
}

export type PlayerResultType = {
  nickname: string
  profileUrl: string
  tier: TireType
  score: number
  item: boolean
  bidSum: number
}

export type GameEndResponseType = {
  roomCode: string
  gameCode: string
  rank: PlayerResultType[]
}
