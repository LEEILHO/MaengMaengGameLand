'use client'

import { Circle } from 'rc-progress'
import { useEffect, useState, useRef } from 'react'
import * as S from '@styles/common/Timer.styled'
import { secondsToMinutesAndSeconds } from '@utils/common/timer'
import { images } from '@constants/images'
import useSound from '@hooks/useSound'
import { sounds } from '@constants/sounds'

type Props = {
  size: string
  fontSize: string
  time: number
  round: number
  timeOverHandle: () => void
}

// todo : 20으로 되어있는 곳 값 props로 받아오기
const Timer = ({ size, fontSize, time, timeOverHandle, round }: Props) => {
  const { playTictocSound, stopTictocSound } = useSound()
  const [currentTime, setCurrentTime] = useState(0)
  const timeRemaining = time - currentTime
  const soundRef = useRef<HTMLAudioElement>(null)

  useEffect(() => {
    console.log('[라운드 변경]', round)
    setCurrentTime(0)
  }, [round, time])

  useEffect(() => {
    if (currentTime < time) {
      const timerId = setTimeout(() => setCurrentTime(currentTime + 1), 1000)
      return () => clearTimeout(timerId) // 타이머 정리
    }
    if (currentTime === time) {
      timeOverHandle()
    }
  }, [currentTime, time, timeOverHandle])

  useEffect(() => {
    if (timeRemaining === 5) {
      // playTictocSound()
      soundRef.current?.play()
    }

    if (timeRemaining === 0) {
      // stopTictocSound()
      soundRef.current?.pause()
    }
  }, [timeRemaining])

  return (
    <S.TimerContainer $size={size}>
      <audio
        src={sounds.common.timer}
        ref={soundRef}
        controls
        loop
        style={{ display: 'none' }}
      />
      <Circle
        percent={(currentTime / time) * 100}
        strokeWidth={6}
        strokeLinecap="round"
        strokeColor={'white'}
      />
      <S.TimerBackGround $type={timeRemaining === 0 ? 'COMMON' : 'SHAKE'}>
        <img src={images.common.header.alarm} alt="timer" />
        <p
          style={{
            color: `${timeRemaining > 5 ? 'white' : 'red'}`,
            fontSize: `${fontSize}px`,
            fontWeight: '700',
            marginBottom: '20px',
          }}
        >
          {secondsToMinutesAndSeconds(timeRemaining)}
        </p>
      </S.TimerBackGround>
    </S.TimerContainer>
  )
}

export default Timer
