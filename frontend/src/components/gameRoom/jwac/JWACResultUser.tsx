import { images } from '@constants/constants'
import * as S from '@styles/gameRoom/jwac/JWACResultUser.styled'

const JWACResultUser = () => {
  return (
    <S.ResultUserContainer>
      <S.Rank>1</S.Rank>
      <S.UserData>
        <S.UserProfileImage src={images.login.maengland} alt="프로필이미지" />
        <S.UserName>ksg2388</S.UserName>
      </S.UserData>
      <S.Score>13</S.Score>
      <S.Money>1200억 3000만 5000원</S.Money>
    </S.ResultUserContainer>
  )
}

export default JWACResultUser
