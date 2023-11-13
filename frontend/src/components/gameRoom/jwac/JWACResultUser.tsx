import { images } from '@constants/constants'
import * as S from '@styles/gameRoom/jwac/JWACResultUser.styled'
import { PlayerResultType } from '@type/gameRoom/jwac.type'
import { formatKoreanCurrency } from '@utils/gameRoom/jwacUtil'

type Props = {
  player: PlayerResultType
}

const JWACResultUser = ({ player }: Props) => {
  return (
    <S.ResultUserContainer>
      <S.Rank>1</S.Rank>
      <S.UserData>
        <S.UserProfileImage src={player.profileUrl} alt="프로필이미지" />
        <S.UserName>{player.nickname}</S.UserName>
      </S.UserData>
      <S.Score>{player.score}</S.Score>
      <S.Money>{formatKoreanCurrency(player.bidSum)}</S.Money>
    </S.ResultUserContainer>
  )
}

export default JWACResultUser
