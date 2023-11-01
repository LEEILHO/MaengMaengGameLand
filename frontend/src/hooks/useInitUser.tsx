'use client'

import { userState } from '@atom/userAtom'
import { UserInformationType } from '@type/common/common.type'
import { authHttp } from '@utils/http'
import { useRecoilState } from 'recoil'

const useInitUser = () => {
  const [user, setUser] = useRecoilState(userState)

  const initUser = () => {
    authHttp.get<UserInformationType>(`user/info`).then((res) => {
      setUser(res)
      console.log(res)
    })
  }

  return initUser
}

export default useInitUser
