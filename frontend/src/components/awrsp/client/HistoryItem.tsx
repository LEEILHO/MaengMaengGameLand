'use client'

import * as S from '@styles/awrsp/HistoryItem.styled'
import { HistoryType } from '@type/awrsp/awrsp.type'
import { getRspImageUrl } from '@utils/awrsp/awrspUtil'

type Props = {
  round: HistoryType
}

const HistoryItem = ({ round }: Props) => {
  return (
    <S.ItemContainer>
      <S.Round>{round.round}</S.Round>
      <S.Result>
        {round.detail.win}승 {round.detail.draw === 1 && '1비김'}
      </S.Result>
      <S.CardList>
        {round.rspList?.map((card, index) => (
          <S.RspCard src={getRspImageUrl(card)} key={card + index} alt={card} />
        ))}
      </S.CardList>
    </S.ItemContainer>
  )
}

export default HistoryItem
