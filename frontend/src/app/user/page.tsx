'use client'

import UserProfileSideBar from '@components/user/UserProfileSideBar'
import Background from 'assets/lotties/background.json'
import * as S from '@styles/user/UserPage.styled'
import RankList from '@components/user/RankList'
import { useEffect } from 'react'
import useInitUser from '@hooks/useInitUser'
import withAuth from '@components/hoc/client/PrivateRoute'

const page = () => {
  const initUser = useInitUser()

  useEffect(() => {
    initUser()
  }, [])

  return (
    <>
      <S.UesrPageContainer>
        <S.BackgroundLottie animationData={Background} loop />
        <UserProfileSideBar />
        <RankList />
      </S.UesrPageContainer>
    </>
  )
}

export default withAuth(page)
