/**
 * 모바일 기기인지 확인하는 메서드
 * @param agent
 * @returns 모바일 기기인 경우 true 반환
 */
export function detectMobileDevice(agent: string) {
  const mobileRegex = [
    /Android/i,
    /iPhone/i,
    /iPad/i,
    /iPod/i,
    /BlackBerry/i,
    /Windows Phone/i,
  ]

  return mobileRegex.some((mobile) => agent.match(mobile))
}

/**
 * IOS 기기인지 확인하는 메서드
 * @param agent
 * @returns IOS 기기인 경우 true 반환
 */
export function detectIosDevice(agent: string) {
  const mobileRegex = [/iPhone/i, /iPad/i, /iPod/i]

  return mobileRegex.some((mobile) => agent.match(mobile))
}
