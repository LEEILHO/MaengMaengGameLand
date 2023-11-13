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

export const TimerState = atom<number>({
  key: 'timerTime',
  default: 30,
})

export const DisplayMessageState = atom<string | null>({
  key: 'displayMessage',
  default: null,
})

export const CurrentPlayerState = atom<String | null>({
  key: 'currentPlayer',
  default: null,
})

export const AllBetChipsState = atom<number>({
  key: 'allBetChips',
  default: 6,
})

export const MyState = atom<PlayerInfoType | null>({
  key: 'my',
  default: null,
})

export const OpponentState = atom<PlayerInfoType | null>({
  key: 'opponent',
  default: null,
})

// 나의 현재까지의 베팅 총 개수
export const MyBetChipsState = atom<number>({
  key: 'myBetChips',
  default: 0,
})

// 상대방의 현재까지의 베팅 총 개수
export const OpponentBetChipsState = atom<number>({
  key: 'opponentBetChips',
  default: 0,
})
