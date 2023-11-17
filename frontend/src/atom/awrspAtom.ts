import {
  DrawCardType,
  GameResultType,
  PlayerResultType,
  HistoryType,
  RspType,
  StepType,
} from '@type/awrsp/awrsp.type'
import { atom } from 'recoil'

export const RspCardListState = atom<RspType[] | null>({
  key: 'rspCardList',
  default: [],
})

export const DrawCardState = atom<DrawCardType>({
  key: 'drawCard',
  default: { drawCard: null, isSetting: true },
})

export const TimerState = atom<number>({
  key: 'timerTime',
  default: 20,
})

export const PlayerResultState = atom<PlayerResultType[] | null>({
  key: 'playerRoundResult',
  default: null,
})

// export const StepState = atom<StepType | null>({
//   key: 'step',
//   default: null,
// })

export const GameResultState = atom<GameResultType[] | null>({
  key: 'awrspGameResult',
  default: null,
})

export const RoundState = atom<number>({
  key: 'round',
  default: 1,
})

export const HistoryState = atom<HistoryType[]>({
  key: 'history',
  default: [],
})
