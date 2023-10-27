import { accessTokenState } from '@atom/userAtom'
import useAxiosInterceptor from '@hooks/useAxiosInterceptor'
import { NextPage } from 'next'
import { useRouter } from 'next/navigation'
import React from 'react'
import { useRecoilValue } from 'recoil'

const withAuth = (Component: NextPage) => {
  const Auth = () => {
    const router = useRouter()
    // 인터셉터 호출할 자리
    useAxiosInterceptor()
    const accessToken = useRecoilValue(accessTokenState)

    if (!accessToken?.accessToken) {
      // 로그인 화면으로 이동
      router.replace('/login')
    }

    return <Component />
  }

  return Auth
}

export default withAuth
