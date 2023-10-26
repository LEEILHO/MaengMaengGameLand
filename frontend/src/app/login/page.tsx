'use client'

import * as S from '@styles/login/Login.styled'
import LoginButton from '@components/login/client/LoginButton'
import rocketAnimation from 'assets/lotties/rocket.json'
import { images } from '@constants/constants'
import { useEffect } from 'react'
import { useRouter } from 'next/navigation'

export default function login() {
  const isLogin = localStorage.getItem('login')
  console.log('')
  const router = useRouter()
  return (
    <S.Login
      onClick={() => {
        if (isLogin) {
          router.push('/test')
        }
      }}
    >
      <S.Title src={images.login.maengland} />
      {!isLogin ? (
        <>
          <S.Announcement>계속하시려면 로그인을 해주세요.</S.Announcement>
          <LoginButton />
        </>
      ) : (
        <S.Announcement>계속하려면 클릭해주세요.</S.Announcement>
      )}
      <S.Rocket animationData={rocketAnimation} loop autoplay={true} />
    </S.Login>
  )
}
