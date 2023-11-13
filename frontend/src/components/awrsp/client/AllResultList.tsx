'use client'

import * as S from '@styles/awrsp/AllResultList.styled'
import AllResultItem from './AllResultItem'
import { PlayerResultState } from '@atom/awrspAtom'
import { useRecoilValue } from 'recoil'
import { useEffect } from 'react'

const AllResultList = () => {
  const playerRoundResult = useRecoilValue(PlayerResultState)
  useEffect(() => {
    console.log(playerRoundResult)
  }, [])

  return (
    <S.AllResultListContainer>
      {playerRoundResult?.map((result) => (
        <AllResultItem key={result.nickname} result={result} />
      ))}
    </S.AllResultListContainer>
  )
}

export default AllResultList
