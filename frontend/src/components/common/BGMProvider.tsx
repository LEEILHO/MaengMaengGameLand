'use client'

import { sounds } from '@constants/sounds'
import { useEffect, useState } from 'react'
import { Howl } from 'howler'
import { useRecoilValue } from 'recoil'
import { soundState } from '@atom/soundAtom'

const BGMProvider = ({ children }: { children: React.ReactNode }) => {
  const isSound = useRecoilValue(soundState)

  const sound = new Howl({
    src: [sounds.lobby.main],
    loop: true,
  })

  useEffect(() => {
    if (isSound?.bgmSound ?? true) {
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
