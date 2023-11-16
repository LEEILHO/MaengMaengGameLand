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

  const flipCardSound = new Howl({
    src: [sounds.awrsp.filpCard],
  })

  const dropCardSound = new Howl({
    src: [sounds.awrsp.dropCard],
  })

  const starSound = new Howl({
    src: [sounds.gsb.star],
  })

  const bettingSound = new Howl({
    src: [sounds.gsb.betting],
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

  const playFlipCardSound = useCallback(() => {
    if (isSound?.effectSound ?? true) {
      flipCardSound.play()
    }
  }, [flipCardSound])

  const playDropCardSound = useCallback(() => {
    if (isSound?.effectSound ?? true) {
      dropCardSound.play()
    }
  }, [dropCardSound])

  const playStarSound = useCallback(() => {
    if (isSound?.effectSound ?? true) {
      starSound.play()
    }
  }, [starSound])

  const playBettingSound = useCallback(() => {
    if (isSound?.effectSound ?? true) {
      bettingSound.play()
    }
  }, [bettingSound])

  return {
    playButtonSound,
    playEnterSound,
    playTabSound,
    playWriteSound,
    playFlipCardSound,
    playDropCardSound,
    playStarSound,
    playBettingSound,
  }
}

export default useSound
