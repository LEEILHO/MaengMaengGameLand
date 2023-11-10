'use client'

import { sounds } from '@constants/sounds'
import { useEffect } from 'react'
import { Howl } from 'howler'

const BGMProvider = ({ children }: { children: React.ReactNode }) => {
  const sound = new Howl({
    src: [sounds.lobby.main],
  })

  useEffect(() => {
    sound.play()
  }, [])

  return <>{children}</>
}

export default BGMProvider
