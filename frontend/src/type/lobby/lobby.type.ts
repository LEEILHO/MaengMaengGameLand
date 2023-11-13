export type CreateRoomRequestType = {
  title: string
  publicRoom: boolean
  host: string
  gameCategory: GameCategoryType
  channelTire: ChannelTireType
}

export type CreateRoomResponseType = {
  roomCode: string
}

export type GameCategoryType =
  | 'ALL_WIN_ROCK_SCISSOR_PAPER'
  | 'JEWELRY_AUCTION'
  | 'GOLD_SILVER_BRONZE'

export type ChannelTireType = 'BRONZE' | 'SILVER' | 'GOLD' | 'UNRANKED'

export type RoomType = {
  roomCode: string
  title: string
  headCount: number
  maxHeadCount: number
}
