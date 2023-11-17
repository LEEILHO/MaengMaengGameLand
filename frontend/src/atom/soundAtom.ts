import { SoundType } from '@type/common/common.type'
import { AtomEffect, atom } from 'recoil'

const localStorageEffect: <T>(key: string) => AtomEffect<T> =
  (key: string) =>
  ({ setSelf, onSet }) => {
    if (typeof window !== 'undefined') {
      const savedValue = localStorage.getItem(key)
      if (savedValue !== null) {
        setSelf(JSON.parse(savedValue))
      }

      onSet((newValue, _, isReset) => {
        if (isReset) return localStorage.removeItem(key)

        return localStorage.setItem(key, JSON.stringify(newValue))
      })
    }
  }

export const soundState = atom<SoundType | null>({
  key: 'soundState',
  default: { bgmSound: true, effectSound: true },
  effects: [localStorageEffect<SoundType | null>('gameSound')],
})
