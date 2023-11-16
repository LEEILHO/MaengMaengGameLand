import { GameCategoryType } from '@type/lobby/lobby.type'

export type UserRecordType = {
  gameCode: string
  gameCategory: GameCategoryType
  rank: number
}
