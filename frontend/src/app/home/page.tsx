'use client'

import * as S from '@styles/home/Home.styled'
import { LottiePlayer } from 'lottie-react'
import Background from 'assets/lotties/background.json'
import HomeHeader from '@components/home/HomeHeader'
import GameCard from '@components/home/GameCard'
import { images } from '@constants/images'
import CButton from '@components/common/clients/CButton'
import { useRouter } from 'next/navigation'
import { useCallback, useEffect } from 'react'
import { GameCategoryType } from '@type/lobby/lobby.type'
import { useSetRecoilState } from 'recoil'
import { gameTypeState } from '@atom/gameAtom'
import useInitUser from '@hooks/useInitUser'
import withAuth from '@components/hoc/client/PrivateRoute'
import useModal from '@hooks/useModal'
import SettingModal from '@components/home/SettingModal'

const Home = () => {
  const router = useRouter()
  const initUser = useInitUser()
  const {
    Modal,
    openModal: openSettingModal,
    closeModal: closeSettingModal,
    isOpen: isSettingOpen,
  } = useModal()

  const handleGameClick = useCallback((url: string) => {
    router.replace(url, { scroll: false })
  }, [])

  useEffect(() => {
    initUser()
  }, [])

  return (
    <>
      <Modal isOpen={isSettingOpen} closeModal={closeSettingModal}>
        <SettingModal closeModal={closeSettingModal}></SettingModal>
      </Modal>
      <S.HomeContainer>
        <HomeHeader
          onClickFriend={() => {}}
          onClickSetting={openSettingModal}
        />
        <S.BackgroundLottie animationData={Background} loop />
        <S.GameCardContainer>
          <GameCard
            backGroundUrl={images.home.gsb}
            name={'금은동'}
            onClick={() => handleGameClick('gsb/lobby')}
          />
          <GameCard
            backGroundUrl={images.home.jwac}
            name={'무제한 보석 경매'}
            onClick={() => handleGameClick('jwac/lobby')}
          />
          <GameCard
            backGroundUrl={images.home.awrsp}
            name={'전승 가위바위보'}
            onClick={() => handleGameClick('awrsp/lobby')}
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

export default withAuth(Home)
