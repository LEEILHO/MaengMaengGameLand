'use client'

import { PlayerResultState, RspCardListState } from '@atom/awrspAtom'
import { userState } from '@atom/userAtom'
import { images } from '@constants/images'
import * as S from '@styles/awrsp/MyResult.styled'
import { getRspImageUrl } from '@utils/awrsp/awrspUtil'
import { useEffect, useState } from 'react'
import { useRecoilValue } from 'recoil'

const MyResult = () => {
  const cardList = useRecoilValue(RspCardListState)
  const playerRoundResult = useRecoilValue(PlayerResultState)
  const user = useRecoilValue(userState)

  useEffect(() => {
    console.log(playerRoundResult)
  }, [])

  return playerRoundResult?.map(
    (result) =>
      result.nickname === user?.nickname && (
        <S.MyResultContainer key={result.nickname}>
          {result.detail ? (
            <>
              <S.WinCount>
                {result.detail.win}승 {result.detail.draw === 1 && '1비김'}
              </S.WinCount>
              <S.CardList>
                {cardList?.map((card, index) => (
                  <S.RspCard
                    src={getRspImageUrl(card)}
                    alt={card}
                    key={card + index}
                  />
                ))}
              </S.CardList>
            </>
          ) : (
            <S.WinCount>미제출</S.WinCount>
          )}
        </S.MyResultContainer>
      ),
  )
}

export default MyResult
