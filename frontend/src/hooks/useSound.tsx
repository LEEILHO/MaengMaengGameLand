import { sounds } from '@constants/sounds'
import { Howl } from 'howler'
import { useCallback } from 'react'

const useSound = () => {
  const buttonSound = new Howl({
    src: [sounds.jwac.button],
  })

  const playButtonSound = useCallback(() => {
    buttonSound.play()
  }, [buttonSound])

  return { playButtonSound }
}

export default useSound
