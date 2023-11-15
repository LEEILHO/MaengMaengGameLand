'use client'

import * as S from '@styles/awrsp/AllResultList.styled'
import AllResultItem from './AllResultItem'
import { PlayerResultState, StepState } from '@atom/awrspAtom'
import { useRecoilValue, useSetRecoilState } from 'recoil'
import { useEffect } from 'react'
import { userState } from '@atom/userAtom'

const AllResultList = () => {
  const playerRoundResult = useRecoilValue(PlayerResultState)
  const user = useRecoilValue(userState)
  const setStep = useSetRecoilState(StepState)

  useEffect(() => {
    if (playerRoundResult) {
      playerRoundResult.map((result) => {
        if (user?.nickname === result.nickname) {
          if (result.finish) {
            console.log('정답을 맞췄다')

            setStep('WAITING')
            return
          }
        }
      })
    }
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
