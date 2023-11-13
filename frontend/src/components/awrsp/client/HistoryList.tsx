'use client'

import * as S from '@styles/awrsp/HistoryList.styled'

import { HistoryState } from '@atom/awrspAtom'
import { useRecoilValue } from 'recoil'
import HistoryItem from './HistoryItem'
import { useEffect } from 'react'

const HistoryList = () => {
  const history = useRecoilValue(HistoryState)

  useEffect(() => {
    console.log(history)
  }, [])

  return (
    <S.ListContainer>
      {history.length !== 0 ? (
        history.map((round) => <HistoryItem key={round.round} round={round} />)
      ) : (
        <S.NoItem>내역이 없습니다.</S.NoItem>
      )}
    </S.ListContainer>
  )
}

export default HistoryList
