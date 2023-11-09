import { JewelryType } from '@type/gameRoom/jwac.type'
import Diamond from 'assets/lotties/diamond.json'
import Sapphire from 'assets/lotties/sapphire.json'
import Ruby from 'assets/lotties/ruby.json'
import Emerald from 'assets/lotties/emerald.json'

export function jewelryToLottie(jewelry: JewelryType) {
  if (jewelry === 'DIAMOND') {
    return Diamond
  }
  if (jewelry === 'SAPPHIRE') {
    return Sapphire
  }
  if (jewelry === 'RUBY') {
    return Ruby
  }
  if (jewelry === 'EMERALD') {
    return Emerald
  }
}

export function formatKoreanCurrency(amount: number): string {
  const koreanUnits = ['조', '억', '만', '원']
  const unit = 10000
  let answer = ''

  while (amount > 0) {
    const mod = amount % unit
    const modToString = mod.toString().replace(/(\d)(\d{3})/, '$1,$2')
    amount = Math.floor(amount / unit)
    answer = `${modToString}${koreanUnits.pop()}${answer}`
  }
  return answer
}
