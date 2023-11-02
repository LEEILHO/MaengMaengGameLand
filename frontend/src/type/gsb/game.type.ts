export type TurnListType = {
  first: boolean
  selected: boolean
}

export type StarStatus = 'in' | 'out'

export type StarType = {
  id: string
  status: StarStatus
  src: string
}

export type StarListType = {
  [key in StarStatus]: StarType[]
}
