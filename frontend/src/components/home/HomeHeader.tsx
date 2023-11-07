'use client'
import { useCallback } from 'react'
import { userState } from '@atom/userAtom'
import { images } from '@constants/images'
import * as S from '@styles/home/Header.styled'
import { useRecoilValue } from 'recoil'
import { TireType } from '@type/common/common.type'

type Props = {
  onClickFriend: () => void
  onClickSetting: () => void
}

const HomeHeader = ({ onClickFriend, onClickSetting }: Props) => {
  const user = useRecoilValue(userState)

  const getTierFrame = useCallback((tier: TireType) => {
    if (tier === 'BRONZE') {
      return images.common.header.bronzeFrame
    }
    if (tier === 'SILVER') {
      return images.common.header.silverFrame
    }
    if (tier === 'GOLD') {
      return images.common.header.goldFrame
    }
    if (tier === 'CHALLENGER') {
      return images.common.header.challengerFrame
    }
  }, [])

  if (!user) return

  return (
    <S.HeaderContainer>
      <S.ProfileCard>
        <S.TierFrame>
          <img className="frame" src={getTierFrame(user.tier)} alt="" />
          <S.ProfileImage src={user?.profile} alt="프로필사진" />
        </S.TierFrame>
        <S.UserName>{user.nickname}</S.UserName>
      </S.ProfileCard>

      <S.FriendsButton onClick={onClickFriend}>
        <img src={images.common.header.friends} alt="친구추가" />
      </S.FriendsButton>

      <S.SettingButton onClick={onClickSetting}>
        <img src={images.common.header.setting} alt="설정" />
      </S.SettingButton>
    </S.HeaderContainer>
  )
}

export default HomeHeader
