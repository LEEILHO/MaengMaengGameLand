import { GameCategoryType } from '@type/lobby/lobby.type'

export type UserRecordResponseType = {
  data: UserRecordType[]
}

export type UserRecordType = {
  gameCode: string
  gameCategory: GameCategoryType
  rank: number
}

export type WatchCodeType = {
  data: {
    watchCode: string
  }
}
