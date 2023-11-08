import * as S from '@styles/gameRoom/jwac/JWACUserList.styled'
import JWACUserItem from './JWACUserItem'
import { Empty } from '@styles/gameRoom/jwac/JWACUserItem.styled'
import { PlayerType } from '@type/gameRoom/jwac.type'

type Prosp = {
  players: PlayerType[]
}

const JWACUserList = ({ players }: Prosp) => {
  const emptyNum = 8 - players.length

  return (
    <S.JWACUserListContainer>
      {players.map((player) => (
        <JWACUserItem key={player.nickname} player={player} />
      ))}
      {Array(emptyNum)
        .fill(0)
        .map((_, index) => (
          <Empty key={index} />
        ))}
    </S.JWACUserListContainer>
  )
}

export default JWACUserList
