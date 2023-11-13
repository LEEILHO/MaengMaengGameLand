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

export const soundState = atom<boolean>({
  key: 'soundState',
  default: true,
  effects: [localStorageEffect<boolean>('bgmSound')],
})
