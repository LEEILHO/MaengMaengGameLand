'use client'

import * as S from '@styles/user/RecordItem.styled'
import { RecordDetailType } from '@type/user/user.type'

type Props = {
  record: RecordDetailType
  gameCategory: string
}

const RecordItem = ({ record, gameCategory }: Props) => {
  return (
    <S.Container>
      <S.Nickname>{record.nickname}</S.Nickname>
      <S.Rank>
        {gameCategory === '금은동'
          ? record.userRank === 1
            ? '승리'
            : '패배'
          : `${record.userRank}등`}
      </S.Rank>
      {record.score && <S.Score>{record.score}점</S.Score>}
    </S.Container>
  )
}

export default RecordItem
