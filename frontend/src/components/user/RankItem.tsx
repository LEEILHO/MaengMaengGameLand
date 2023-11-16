import * as S from '@styles/user/RankItem.styled'
import { UserRecordType } from '@type/user/user.type'

type Props = {
  record: UserRecordType
}

const RankItem = ({ record }: Props) => {
  const changeGameName = () => {
    if (record.gameCategory === 'ALL_WIN_ROCK_SCISSOR_PAPER') {
      return '전승 가위바위보'
    }
    if (record.gameCategory === 'GOLD_SILVER_BRONZE') {
      return '금은동'
    }
    if (record.gameCategory === 'JEWELRY_AUCTION') {
      return '무제한 보석 경매'
    }
  }

  return (
    <S.RankItemContainer>
      <S.RankText>{record.rank}</S.RankText>
      <S.GameGategoryText>{changeGameName()}</S.GameGategoryText>
    </S.RankItemContainer>
  )
}

export default RankItem
