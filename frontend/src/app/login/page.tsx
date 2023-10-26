'use client'

import * as S from '@styles/login/Login.styled'
import LoginButton from '@components/login/client/LoginButton'
import rocketAnimation from 'assets/lotties/rocket.json'
import { images } from '@constants/constants'

export default function login() {
  return (
    <S.Login>
      <S.Title src={images.login.maengland} />
      <S.Announcement>계속하시려면 로그인을 해주세요.</S.Announcement>
      <LoginButton />
      <S.Rocket animationData={rocketAnimation} loop autoplay={true} />
    </S.Login>
  )
}
