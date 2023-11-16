import * as S from '@styles/user/RankList.styled'
import RankItem from './RankItem'
import { useState, useEffect } from 'react'
import { UserRecordType } from '@type/user/user.type'
import { getRecordHistory } from 'apis/user/userApi'

const RankList = () => {
  const [records, setRecords] = useState<UserRecordType[]>([])

  useEffect(() => {
    getRecordHistory().then((res) => {
      setRecords(res)
    })
  }, [])

  return (
    <S.RankListContainer>
      <S.TitleRow>
        <S.RankTitle>등수</S.RankTitle>
        <S.GameCategoryTitle>게임 종류</S.GameCategoryTitle>
      </S.TitleRow>
      {records.map((record) => (
        <RankItem record={record} />
      ))}
    </S.RankListContainer>
  )
}

export default RankList
