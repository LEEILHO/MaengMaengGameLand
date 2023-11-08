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
  const units = ['억', '만', '천', '백', '십', '원']
  const digits = amount.toString().split('').reverse()
  let result = ''

  if (amount === -1) return '0원'

  for (let i = 0; i < digits.length; i++) {
    if (digits[i] !== '0') {
      result = digits[i] + units[i] + result
    } else if (i === 4) {
      result = '만' + result
    } else if (i === 8) {
      result = '억' + result
    }
  }

  if (amount < 10000) {
    result += '원'
  }

  return result
}
