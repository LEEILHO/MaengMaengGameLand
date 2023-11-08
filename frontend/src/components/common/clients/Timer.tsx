'use client'

import { Circle } from 'rc-progress'
import { useEffect, useState } from 'react'
import * as S from '@styles/common/Timer.styled'
import { secondsToMinutesAndSeconds } from '@utils/common/timer'
import { images } from '@constants/images'
import { css } from 'styled-components'
import { RoundDataType } from '@type/gameRoom/jwac.type'

type Props = {
  size: string
  fontSize: string
  roundData: RoundDataType
  timeOverHandle: () => void
}

// todo : 20으로 되어있는 곳 값 props로 받아오기
const Timer = ({ size, fontSize, roundData, timeOverHandle }: Props) => {
  const [currentTime, setCurrentTime] = useState(0)
  const timeRemaining = roundData.time - currentTime

  useEffect(() => {
    if (currentTime < roundData.time) {
      const timerId = setTimeout(() => setCurrentTime(currentTime + 1), 1000)
      return () => clearTimeout(timerId) // 타이머 정리
    }
    if (currentTime === roundData.time) {
      console.log('타임 오버')
      timeOverHandle()
    }
  }, [currentTime])

  useEffect(() => {
    console.log(roundData.time - currentTime)
    // console.log('남은 시간', timeRemaining)
  }, [currentTime])

  return (
    <S.TimerContainer $size={size}>
      <Circle
        percent={(currentTime / roundData.time) * 100}
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
