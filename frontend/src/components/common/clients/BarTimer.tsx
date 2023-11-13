'use client'

import { useEffect, useState } from 'react'
import * as S from '@styles/common/BarTimer.styled'
import { secondsToMinutesAndSeconds } from '@utils/common/timer'
import { images } from '@constants/images'

type Props = {
  height?: number
  width?: number
  fontSize?: number
  time: number
}

const BarTimer = ({ height, width, fontSize, time }: Props) => {
  const [currentTime, setCurrentTime] = useState(0)
  const timeRemaining = time - currentTime

  useEffect(() => {
    setCurrentTime(0)
  }, [time])

  useEffect(() => {
    if (currentTime < time) {
      const timerId = setTimeout(() => setCurrentTime(currentTime + 1), 1000)
      return () => clearTimeout(timerId) // 타이머 정리
    }
    // if (currentTime === time) {
    //   console.log('타임 오버')
    // }
  }, [currentTime])

  return (
    <S.BarTimerContainer>
      <img src={images.common.header.timer} />
      {secondsToMinutesAndSeconds(timeRemaining)}
    </S.BarTimerContainer>
  )
}

export default BarTimer
