'use client'

import { images } from '@constants/images'
import * as S from '@styles/awrsp/AllResultItem.styled'
import { PlayerResultType } from '@type/awrsp/awrsp.type'

type Props = {
  result: PlayerResultType
}

const AllResultItem = ({ result }: Props) => {
  return (
    <S.AllResultItemContainer>
      <S.ProfileImage src={images.common.header.dummyProfile} />
      <S.Nickname>{result.nickname}</S.Nickname>
      {result.detail ? (
        <S.WinCount>
          {result.detail.win}승 {result.detail.draw === 1 && '1비김'}
        </S.WinCount>
      ) : (
        <S.WinCount>미제출</S.WinCount>
      )}

      {result.finish && <S.Winner>{result.rank}등</S.Winner>}
    </S.AllResultItemContainer>
  )
}

export default AllResultItem
