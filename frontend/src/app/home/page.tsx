'use client'

import * as S from '@styles/home/Home.styled'
import { LottiePlayer } from 'lottie-react'
import Backround from 'assets/lotties/background.json'
import HomeHeader from '@components/home/HomeHeader'
import GameCard from '@components/home/GameCard'
import { images } from '@constants/images'
import CButton from '@components/common/clients/CButton'
import { useRouter } from 'next/navigation'

const Home = () => {
  const router = useRouter()

  return (
    <>
      <S.HomeContainer>
        <HomeHeader onClickFriend={() => {}} onClickSetting={() => {}} />
        <S.BackgroundLottie animationData={Backround} loop />
        <S.GameCardContainer>
          <GameCard
            backGroundUrl={images.home.gsb}
            name={'금은동'}
            onClick={() => router.push('gsb/lobby', { scroll: false })}
          />
          <GameCard
            backGroundUrl={images.home.jwac}
            name={'무제한 보석 경매'}
            onClick={() => router.push('jwac/lobby', { scroll: false })}
          />
          <GameCard
            backGroundUrl={images.home.awrsp}
            name={'전승 가위바위보'}
            onClick={() => router.push('awrsp/lobby', { scroll: false })}
          />
        </S.GameCardContainer>
        <S.ButtonLow>
          <CButton
            backgroundColor="rgba(112, 0, 255, 1)"
            fontSize={14}
            radius={32}
            text="초대 코드로 입장"
            color="white"
          />
        </S.ButtonLow>
      </S.HomeContainer>
    </>
  )
}

export default Home
