import { RoomType } from '@type/lobby/lobby.type'
import { atom } from 'recoil'

export const roomsState = atom<RoomType[] | null>({
  key: 'rooms',
  default: null,
})
