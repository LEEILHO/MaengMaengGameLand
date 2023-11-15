import { soundState } from '@atom/soundAtom'
import { sounds } from '@constants/sounds'
import { Howl } from 'howler'
import { useCallback } from 'react'
import { useRecoilValue } from 'recoil'

const useSound = () => {
  const isSound = useRecoilValue(soundState)

  const buttonSound = new Howl({
    src: [sounds.jwac.button],
  })

  const enterSound = new Howl({
    src: [sounds.jwac.enter],
  })

  const tapSound = new Howl({
    src: [sounds.jwac.tab],
  })

  const writeSound = new Howl({
    src: [sounds.jwac.write],
  })

  const playButtonSound = useCallback(() => {
    if (isSound?.effectSound ?? true) {
      buttonSound.play()
    }
  }, [buttonSound])

  const playEnterSound = useCallback(() => {
    if (isSound?.effectSound ?? true) {
      enterSound.play()
    }
  }, [enterSound])

  const playTabSound = useCallback(() => {
    if (isSound?.effectSound ?? true) {
      tapSound.play()
    }
  }, [tapSound])

  const playWriteSound = useCallback(() => {
    if (isSound?.effectSound ?? true) {
      writeSound.play()
    }
  }, [tapSound])
  return {
    playButtonSound,
    playEnterSound,
    playTabSound,
    playWriteSound,
  }
}

export default useSound
