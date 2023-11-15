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

  const tictocSound = new Audio(sounds.common.timer)

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

  const playTictocSound = useCallback(() => {
    if (isSound?.effectSound ?? true) {
      console.log('시계 ㄱㄱ', tictocSound)
      tictocSound.play()
    }
  }, [tictocSound])

  const stopTictocSound = useCallback(() => {
    if (isSound?.effectSound ?? true) {
      console.log('시간아 꺼져', tictocSound)
      tictocSound.pause()
    }
  }, [tictocSound])
  return {
    playButtonSound,
    playEnterSound,
    playTabSound,
    playWriteSound,
    playTictocSound,
    stopTictocSound,
  }
}

export default useSound
