export type UserInformationType = {
  userEmail: string
  nickname: string
  profile: string
  score: number
  tier: TireType
  win: number
  lose: number
}

export type TireType = 'BRONZE' | 'SILVER' | 'GOLD' | 'CHALLENGER'
