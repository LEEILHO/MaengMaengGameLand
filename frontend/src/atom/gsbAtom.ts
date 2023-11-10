import {
  Player,
  PlayerInfoType,
  RoundType,
  TurnListType,
} from '@type/gsb/gsb.type'
import { atom } from 'recoil'

export const TurnCardState = atom<TurnListType[] | null>({
  key: 'turnCard',
  default: null,
})

export const RoundState = atom<RoundType | null>({
  key: 'round',
  default: null,
})

export const DisplayMessageState = atom<string | null>({
  key: 'displayMessage',
  default: null,
})

export const CurrentPlayerState = atom<String | null>({
  key: 'currentPlayer',
  default: null,
})

export const AllBetChipsState = atom<number | null>({
  key: 'allBetChips',
  default: null,
})

export const MyState = atom<PlayerInfoType | null>({
  key: 'my',
  default: null,
})

export const OpponentState = atom<PlayerInfoType | null>({
  key: 'opponent',
  default: null,
})
