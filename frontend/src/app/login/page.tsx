'use client'

import * as S from '@styles/login/Login.styled'
import rocketAnimation from 'assets/lotties/rocket.json'
import { images } from '@constants/constants'
import { useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'
import { PrintLoginBtn } from '@utils/login/PrintLoginBtn'

export default function login() {
  const [isLogin, setIsLogin] = useState<null | boolean>(null)
  console.log('')
  const router = useRouter()

  useEffect(() => {
    setIsLogin(localStorage.getItem('login') === 'true')
  }, [])

  return (
    <S.Login
      onClick={() => {
        if (isLogin) {
          router.push('/home')
        }
      }}
    >
      <S.Title src={images.login.maengland} />
      <PrintLoginBtn isLogin={isLogin} />
      <S.Rocket animationData={rocketAnimation} loop autoplay={true} />
    </S.Login>
  )
}
