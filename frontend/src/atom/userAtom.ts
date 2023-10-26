import { ResponseAccessTokenType } from '@type/common/auth.type'
import { UserInformationType } from '@type/common/common.type'
import { atom } from 'recoil'
export const accessTokenState = atom<ResponseAccessTokenType | null>({
  key: 'accessToken', // unique ID (with respect to other atoms/selectors)
  default: null, // default value (aka initial value)
})

export const userState = atom<UserInformationType | null>({
  key: 'user',
  default: null,
})
