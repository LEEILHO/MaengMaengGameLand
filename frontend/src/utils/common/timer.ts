/**
 * 초를 받아서 분 : 초 형태로 반환해준다.
 * @param totalSeconds
 * @returns
 */
export function secondsToMinutesAndSeconds(totalSeconds: number): string {
  if (totalSeconds < 0) {
    return 'Invalid Input' // 음수 초 값에 대한 오류 처리
  }

  const minutes = Math.floor(totalSeconds / 60)
  const seconds = totalSeconds % 60

  const minutesString = String(minutes).padStart(2, '0')
  const secondsString = String(seconds).padStart(2, '0')

  return `${minutesString} : ${secondsString}`
}
