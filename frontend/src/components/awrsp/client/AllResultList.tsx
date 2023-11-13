'use client'

import * as S from '@styles/awrsp/AllResultList.styled'
import AllResultItem from './AllResultItem'
import { PlayerResultState, StepState } from '@atom/awrspAtom'
import { useRecoilValue, useSetRecoilState } from 'recoil'
import { useEffect } from 'react'
import { userState } from '@atom/userAtom'

const AllResultList = () => {
  const playerRoundResult = useRecoilValue(PlayerResultState)
  const setStep = useSetRecoilState(StepState)
  const user = useRecoilValue(userState)
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
