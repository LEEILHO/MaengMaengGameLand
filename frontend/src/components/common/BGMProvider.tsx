'use client'

import { sounds } from '@constants/sounds'
import { useEffect, useState } from 'react'
import { Howl } from 'howler'
import { useRecoilValue } from 'recoil'
import { soundState } from '@atom/soundAtom'
import { detectMobileDevice } from '@utils/common/mobile'
import { usePathname } from 'next/navigation'

const BGMProvider = ({ children }: { children: React.ReactNode }) => {
  const pathname = usePathname()
  const isSound = useRecoilValue(soundState)
  const sound = new Howl({
    src: [sounds.lobby.main],
    loop: true,
  })

  useEffect(() => {
    const isIndex = pathname.split('/')[1] === '' ? true : false

    // 인덱스 페이지가 아닌 경우만 배경음악 재생
    if ((!isIndex && isSound?.bgmSound) ?? true) {
      sound.play()
    } else {
      sound.stop()
    }

    return () => {
      sound.stop()
    }
  }, [isSound])

  return <>{children}</>
}

export default BGMProvider
