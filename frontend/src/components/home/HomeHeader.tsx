'use client'
import { images } from '@constants/images'
import * as S from '@styles/home/Header.styled'

type Props = {
  onClickFriend: () => void
  onClickSetting: () => void
}

const HomeHeader = ({ onClickFriend, onClickSetting }: Props) => {
  return (
    <S.HeaderContainer>
      <S.ProfileCard>
        <S.TierFrame>
          <img className="frame" src={images.common.header.goldFrame} alt="" />
          <S.ProfileImage
            src={images.common.header.dummyProfile}
            alt="프로필사진"
          />
        </S.TierFrame>
        <S.UserName>김상근김상근김상근김상근</S.UserName>
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
