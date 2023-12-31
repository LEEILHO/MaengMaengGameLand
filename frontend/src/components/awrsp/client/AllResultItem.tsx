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
      <S.ProfileImage src={result.profileUrl} />
      <S.Nickname>{result.nickname}</S.Nickname>
      {!result.finish &&
        (result.detail ? (
          <S.WinCount>
            {result.detail.win}승 {result.detail.draw === 1 && '1비김'}
          </S.WinCount>
        ) : (
          <S.WinCount>미제출</S.WinCount>
        ))}

      {result.finish && (
        <S.Winner>
          <p>{result.rank}등</p>
        </S.Winner>
      )}
    </S.AllResultItemContainer>
  )
}

export default AllResultItem
