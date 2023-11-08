import {
  JewelryItemType,
  PlayerType,
  RoundDataType,
  RoundResultType,
} from '@type/gameRoom/jwac.type'
import { atom } from 'recoil'

export const jwacPlayerListState = atom<PlayerType[]>({
  key: 'jwacPlayerList',
  default: [],
})

export const jwacRoundState = atom<RoundDataType | null>({
  key: 'jwacRound',
  default: null,
})

export const jwacRoundResultState = atom<RoundResultType | null>({
  key: 'jwacTotalRound',
  default: null,
})

export const jwacJewelryItemState = atom<JewelryItemType | null>({
  key: 'jwacJewelryItem',
  default: null,
})
