'use client'
import { userState } from '@atom/userAtom'
import { images } from '@constants/images'
import * as S from '@styles/home/Header.styled'
import { useRecoilValue } from 'recoil'

type Props = {
  onClickFriend: () => void
  onClickSetting: () => void
}

const HomeHeader = ({ onClickFriend, onClickSetting }: Props) => {
  const user = useRecoilValue(userState)

  return (
    <S.HeaderContainer>
      <S.ProfileCard>
        <S.TierFrame>
          <img className="frame" src={images.common.header.goldFrame} alt="" />
          <S.ProfileImage src={user?.profile} alt="프로필사진" />
        </S.TierFrame>
        <S.UserName>{user?.nickname}</S.UserName>
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
