export type RspType =
  | 'ROCK'
  | 'SCISSOR'
  | 'PAPER'
  | 'DRAW_ROCK'
  | 'DRAW_SCISSOR'
  | 'DRAW_PAPER'
  | 'EMPTY'

export type CardStatus = 'in' | 'out'

export type CardType = {
  id: string
  status: CardStatus
  rsp: RspType
}

export type CardListType = {
  [key in CardStatus]: CardType[]
}

export type StepType =
  | 'ENTER_GAME'
  | 'DRAW_CARD'
  | 'CARD_SUBMIT'
  | 'PLAYER_WINS'
  | 'WAITING'
  | 'ALL_WINS'
  | 'GAME_OVER'

export type RoundResultType = {
  win: number
  draw: number
}

export type PlayerResultType = {
  nickname: string
  profileUrl: string
  finish: boolean
  rank: number
  detail: RoundResultType
}

export type GameResultType = {
  nickname: string
  round: number
  rank: number
}

export type DrawCardType = {
  drawCard: RspType | null
  isSetting: boolean
}

export type HistoryType = {
  round: number
  detail: RoundResultType
  rspList: RspType[] | null
}
