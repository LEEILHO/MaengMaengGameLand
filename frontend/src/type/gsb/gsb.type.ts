export type TurnListType = {
  seq: number
  nickname: string | null
  selected: boolean
}

export type RoundType =
  | 'ChoiceTurn'
  | 'Combination'
  | 'Waiting'
  | 'Betting'
  | 'Result'

export type StarStatus = 'in' | 'out'

export type StarType = {
  id: string
  status: StarStatus
  src: string
}

export type StarListType = {
  [key in StarStatus]: StarType[]
}
