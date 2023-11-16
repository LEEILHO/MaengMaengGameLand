import { RoundType } from '@type/gsb/gsb.type'

export const getMs = (round: RoundType) => {
  if (round === 'CombWaiting' || round === 'Combination') return 0
  if (round === 'GiveUpResult' || round === 'DrawResult') return 3000
  if (round === 'Result') return 5000
}
