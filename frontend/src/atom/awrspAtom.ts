import { RspType } from '@type/awrsp/awrsp.type'
import { atom } from 'recoil'

export const RspCardListState = atom<RspType[] | null>({
  key: 'rspCardList',
  default: [],
})

export const DrawCardState = atom<RspType | null>({
  key: 'drawCard',
  default: null,
})

export const TimerState = atom<number>({
  key: 'timerTime',
  default: 0,
})
