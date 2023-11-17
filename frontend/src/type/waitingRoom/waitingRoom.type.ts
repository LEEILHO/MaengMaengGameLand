import { GameCategoryType } from '@type/lobby/lobby.type'
import { SeatInfo } from './seat.type'

export type RoomCodeType = {
  roomCode: string
}

export type ChatMessageType = {
  nickname: string
  message: string
}

export type RoomInfoType = {
  title: string
  headCount: number
  publicRoom: boolean
  participant: SeatInfo[]
  gameCategory: GameCategoryType
}

export type KickPlayerType = {
  nickname: string
}
