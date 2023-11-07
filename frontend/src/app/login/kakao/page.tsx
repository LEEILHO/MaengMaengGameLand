'use client'

import { accessTokenState } from '@atom/userAtom'
import { ResponseAccessTokenType } from '@type/common/auth.type'
import { http } from '@utils/http'
import { useRouter } from 'next/navigation'
import { useEffect } from 'react'
import { useSetRecoilState } from 'recoil'

import * as S from '@styles/login/LoginRedirect.styled'

export default function loginRedirect() {
  const setAccessToken = useSetRecoilState(accessTokenState)
  const router = useRouter()
  useEffect(() => {
    const code = new URL(document.location.toString()).searchParams.get('code')

    if (code) {
      http
        .post<ResponseAccessTokenType>(`auth/kakao`, { code: code })
        .then((res) => {
          console.log('response: ', res)
          setAccessToken({ accessToken: res.accessToken })
          localStorage.setItem('login', 'true')
          router.replace('/home')
        })
        .catch((err) => {
          console.log('error: ', err)
          router.replace('/login')
        })
    }
  }, [])

  return (
    <S.LoginRedirect>
      <S.Loading>로그인 중 입니다...</S.Loading>
    </S.LoginRedirect>
  )
}
