'use client'

import { RspCardListState } from '@atom/awrspAtom'
import { images } from '@constants/images'
import * as S from '@styles/awrsp/MyResult.styled'
import { RspType } from '@type/awrsp/awrsp.type'
import { getRspImageUrl } from '@utils/awrsp/awrspUtil'
import { useCallback, useEffect } from 'react'
import { useRecoilState } from 'recoil'

const MyResult = () => {
  const [cardList, setCardList] = useRecoilState(RspCardListState)

  useEffect(() => {
    setCardList([
      'ROCK',
      'SCISSOR',
      'ROCK',
      'PAPER',
      'DRAW_PAPER',
      'PAPER',
      'SCISSOR',
      'ROCK',
    ])
  }, [])

  return (
    <S.MyResultContainer>
      <S.WinCount>3승</S.WinCount>
      <S.CardList>
        {cardList?.map((card, index) => (
          <S.RspCard src={getRspImageUrl(card)} alt={card} key={card + index} />
        ))}
      </S.CardList>
    </S.MyResultContainer>
  )
}

export default MyResult
