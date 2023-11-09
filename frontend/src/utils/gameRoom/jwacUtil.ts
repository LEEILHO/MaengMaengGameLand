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
  if (amount === 0) return '0원'

  const units = ['해', '조', '억', '만', '천', '백', '십', '원']
  const digits = amount.toString().split('').reverse()
  let result = ''
  let unitIndex = 0 // 단위 인덱스

  for (let i = 0; i < digits.length; i++) {
    if (digits[i] !== '0') {
      result = digits[i] + units[unitIndex] + result
    }

    if (i > 0 && i % 4 === 0) {
      unitIndex++
    }
  }

  return result
}
