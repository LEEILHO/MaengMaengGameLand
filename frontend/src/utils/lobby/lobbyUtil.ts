import { type } from 'os'

export function channelTypeChange(type: '자유' | '브론즈' | '실버' | '골드') {
  if (type === '자유') {
    return 'UNRANK'
  }
  if (type === '브론즈') {
    return 'BRONZE'
  }
  if (type === '실버') {
    return 'SILVER'
  }
  return 'GOLD'
}

export function gameTypeChange(type: string) {
  if (type === 'jwac') {
    return 'JEWELRY_AUCTION'
  }
  if (type === 'gsb') {
    return 'ALL_WIN_ROCK_SCISSOR_PAPER'
  }
  return 'GOLD_SILVER_BRONZE'
}
