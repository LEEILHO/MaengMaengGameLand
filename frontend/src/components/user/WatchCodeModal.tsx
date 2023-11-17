'use client'

import { images } from '@constants/images'
import * as S from '@styles/user/WatchCodeModal.styled'
import { getWatchLoginCode } from 'apis/user/userApi'
import { useEffect, useState } from 'react'

type Props = {
  closeModal: () => void
}

const WatchCodeModal = ({ closeModal }: Props) => {
  const [code, setCode] = useState<string | null>(null)
  const [currentTime, setCurrentTime] = useState(0)
  const [time, setTime] = useState(180)
  const timeRemaining = time - currentTime

  const generateMinuteSecond = (time: number) => {
    const minute = Math.floor(time / 60)
    const second = Math.floor(time % 60)
    return `${minute}분 ${second}초`
  }

  const getWatchCode = () => {
    getWatchLoginCode().then((res) => {
      setCode(res.data.watchCode)
    })
  }

  useEffect(() => {
    getWatchCode()
  }, [])

  useEffect(() => {
    if (currentTime < time) {
      const timerId = setTimeout(() => setCurrentTime(currentTime + 1), 1000)
      return () => clearTimeout(timerId) // 타이머 정리
    }
    if (currentTime === time) {
      getWatchCode()
      setCurrentTime(0)
      setTime(180)
    }
  }, [currentTime, time])

  return (
    <S.ModalContainer>
      <S.TopRow>
        <S.Title>워치 코드 발급</S.Title>
        <S.CloseIcon
          src={images.lobby.close}
          alt={'워치 발급 코드 창 닫기'}
          onClick={closeModal}
        />
      </S.TopRow>
      <S.CodeRow>
        <S.Code>{code}</S.Code>
      </S.CodeRow>
      <S.TimerRow>
        <S.Timer>{generateMinuteSecond(timeRemaining)}</S.Timer>
      </S.TimerRow>
    </S.ModalContainer>
  )
}

export default WatchCodeModal
