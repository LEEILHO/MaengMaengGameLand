'use client'

import { images } from '@constants/images'
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
      {round.detail ? (
        <S.Result>
          {round.detail.win}승 {round.detail.draw === 1 && '1비김'}
        </S.Result>
      ) : (
        <S.Result>미제출</S.Result>
      )}
      <S.CardList>
        {round.detail
          ? round.rspList?.map((card, index) => (
              <S.RspCard
                src={getRspImageUrl(card)}
                key={card + index}
                alt={card}
              />
            ))
          : [...Array(7)].map((_, index) => (
              <S.RspCard
                src={images.gsb.cardBack}
                key={'cardBack' + index}
                alt={'미제출'}
              />
            ))}
      </S.CardList>
    </S.ItemContainer>
  )
}

export default HistoryItem
