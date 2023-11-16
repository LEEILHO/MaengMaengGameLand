import { GameCategoryType } from '@type/lobby/lobby.type'

export type UserRecordResponseType = {
  data: UserRecordType[]
}

export type RecordDetailResponseType = {
  data: RecordDetailType[]
}

export type RecordDetailType = {
  nickname: string
  score: null
  userRank: number
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
