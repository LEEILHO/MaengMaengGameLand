export type Participant = {
  nickname: string
  ready: boolean
  host: boolean
  profileUrl: string
  tier: string
}

export type SeatInfo = {
  open: boolean
  user: Participant
}
