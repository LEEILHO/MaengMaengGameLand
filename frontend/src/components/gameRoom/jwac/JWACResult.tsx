import * as S from '@styles/gameRoom/jwac/JWACResult.styled'
import JWACResultUser from './JWACResultUser'
import { images } from '@constants/images'
import { PlayerResultType } from '@type/gameRoom/jwac.type'

type Props = {
  gameResult: PlayerResultType[]
}

const JWACResult = ({ gameResult }: Props) => {
  return (
    <S.JWACResultContainer>
      <S.DiscriptionRow>
        <S.UserDataTitle>유저 정보</S.UserDataTitle>
        <S.ScoreTitle>점수</S.ScoreTitle>
        <S.MoneyTitle>사용 금액</S.MoneyTitle>
      </S.DiscriptionRow>
      <S.PlayerListContainer>
        {gameResult.map((player) => (
          <JWACResultUser player={player} />
        ))}
      </S.PlayerListContainer>
    </S.JWACResultContainer>
  )
}

export default JWACResult
