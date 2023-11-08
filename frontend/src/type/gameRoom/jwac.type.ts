import { TireType } from '@type/common/common.type'

export interface PlayerResponseType {
  nickname: string
  profileUrl: string
  tier: TireType
}

export interface GameInfoType {
  gameCode: string
  playerInfo: PlayerResponseType[]
}

export interface PlayerScoreType {
  nickname: string
  score: number
  item: boolean
  bidSum: number
}

export interface PlayerType extends PlayerResponseType {
  score: number
  item: boolean
  bidSum: number
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
  players: PlayerScoreType[]
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
