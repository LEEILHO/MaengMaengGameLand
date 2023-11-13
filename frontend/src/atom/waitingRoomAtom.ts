import { RoomInfoType } from '@type/waitingRoom/waitingRoom.type'
import { atom } from 'recoil'

export const RoomInfoState = atom<RoomInfoType | null>({
  key: 'roomInfo',
  default: null,
})
