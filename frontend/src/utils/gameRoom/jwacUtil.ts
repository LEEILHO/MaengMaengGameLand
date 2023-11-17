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

  if (amount === 0) return '0원'

  while (amount > 0) {
    const mod = amount % unit
    const modToString = mod.toString().replace(/(\d)(\d{3})/, '$1,$2')
    amount = Math.floor(amount / unit)
    if (modToString === '0') {
      koreanUnits.pop()
    } else {
      answer = `${modToString}${koreanUnits.pop()} ${answer}`
    }
  }

  return answer
}

/**
 *
 * @param input
 * @returns 돈을 입력받아 숫자만 반환 (ex. 100,000,000원 => 100000000 반환)
 */
export function extractNumberFromString(input: string): number {
  // ','와 '원'을 제거하고 공백도 제거합니다.
  const cleanedString = input.replace(/,|원|\s/g, '')

  const extractedNumber = parseInt(cleanedString, 10)

  return extractedNumber
}
