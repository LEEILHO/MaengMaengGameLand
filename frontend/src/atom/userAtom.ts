import { ResponseAccessTokenType } from '@type/common/auth.type'
import { atom } from 'recoil'
export const accessTokenState = atom<ResponseAccessTokenType | null>({
  key: 'accessToken', // unique ID (with respect to other atoms/selectors)
  default: null, // default value (aka initial value)
})
