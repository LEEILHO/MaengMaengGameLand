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
