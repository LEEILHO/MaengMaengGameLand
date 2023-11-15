'use client'

import {
  HistoryState,
  PlayerResultState,
  RoundState,
  RspCardListState,
  StepState,
} from '@atom/awrspAtom'
import { userState } from '@atom/userAtom'
import { images } from '@constants/images'
import * as S from '@styles/awrsp/MyResult.styled'
import { HistoryType, PlayerResultType } from '@type/awrsp/awrsp.type'
import { getRspImageUrl } from '@utils/awrsp/awrspUtil'
import { useEffect, useState } from 'react'
import { useRecoilState, useRecoilValue, useSetRecoilState } from 'recoil'

const MyResult = () => {
  const cardList = useRecoilValue(RspCardListState)
  const playerRoundResult = useRecoilValue(PlayerResultState)
  const user = useRecoilValue(userState)
  const [history, setHistory] = useRecoilState(HistoryState)
  const round = useRecoilValue(RoundState)
  const setStep = useSetRecoilState(StepState)
  const [myResult, setMyResult] = useState<PlayerResultType>()

  useEffect(() => {
    console.log(playerRoundResult)

    playerRoundResult?.map((result) => {
      if (result.nickname === user?.nickname) {
        setMyResult(result)
        const newHistory: HistoryType = {
          round: round,
          detail: result.detail,
          rspList: cardList,
        }

        setHistory([...history, newHistory])
        return
      }
    })

    return () => {
      if (myResult && myResult.finish) {
        setStep('WAITING')
        return
      }
    }
  }, [])

  if (!myResult) return null

  return (
    <S.MyResultContainer>
      {myResult.detail ? (
        <>
          <S.WinCount>
            {myResult.detail.win}승 {myResult.detail.draw === 1 && '1비김'}
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
  )
}

export default MyResult
