import { ChannelTireType, GameCategoryType } from '@type/lobby/lobby.type'
import { atom } from 'recoil'

export const channelState = atom<ChannelTireType | null>({
  key: 'channel',
  default: null,
})

export const gameTypeState = atom<GameCategoryType | null>({
  key: 'game',
  default: null,
})
