import { images } from '@constants/images'
import * as S from '@styles/gameRoom/jwac/JWACUserItem.styled'

const JWACUserItem = () => {
  return (
    <S.JWACUserItemContainer>
      <S.UserProfile src={images.waitingRoom.dummyRabbit} alt="프로필" />
      <S.Username>김상근</S.Username>
      <S.UserScore>13점</S.UserScore>
    </S.JWACUserItemContainer>
  )
}

export default JWACUserItem
