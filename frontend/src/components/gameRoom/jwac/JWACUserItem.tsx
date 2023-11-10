import { images } from '@constants/images'
import * as S from '@styles/gameRoom/jwac/JWACUserItem.styled'
import { PlayerType } from '@type/gameRoom/jwac.type'

type Props = {
  player: PlayerType
}

const JWACUserItem = ({ player }: Props) => {
  return (
    <S.JWACUserItemContainer>
      <S.UserProfile src={player.profileUrl} alt="프로필" />
      <S.Username>{player.nickname}</S.Username>
      <S.UserScore>{`${player.score}점`}</S.UserScore>
    </S.JWACUserItemContainer>
  )
}

export default JWACUserItem
