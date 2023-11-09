import * as S from '@styles/gameRoom/jwac/JWACResult.styled'
import JWACResultUser from './JWACResultUser'
import { images } from '@constants/images'

const JWACResult = () => {
  return (
    <S.JWACResultContainer>
      <S.DiscriptionRow>
        <S.UserDataTitle>유저 정보</S.UserDataTitle>
        <S.ScoreTitle>점수</S.ScoreTitle>
        <S.MoneyTitle>사용 금액</S.MoneyTitle>
      </S.DiscriptionRow>
      <S.PlayerListContainer>
        {Array(8)
          .fill(0)
          .map(() => (
            <JWACResultUser />
          ))}
      </S.PlayerListContainer>
    </S.JWACResultContainer>
  )
}

export default JWACResult
