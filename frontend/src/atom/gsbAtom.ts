import { RoundType, TurnListType } from '@type/gsb/gsb.type'
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
