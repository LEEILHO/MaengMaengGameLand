'use client'

import { images } from '@constants/images'
import * as S from '@styles/gsb/PlayerResultCard.styled'

type Props = {
  nickname: string
  profileUrl: string
  chips: number
  result: string
}

const PlayerReultCard = ({ nickname, profileUrl, chips, result }: Props) => {
  return (
    <S.Container result={result}>
      <S.ResultTitle result={result}>{result}</S.ResultTitle>
      <S.ProfileImage src={profileUrl} alt="프로필 사진" />
      <S.Nickname>{nickname}</S.Nickname>
      <S.ChipsRow>
        <img className="chip" src={images.gsb.chip} alt="chip" />
        <img className="mul" src={images.gsb.mul} alt="곱하기" />
        <p className="chipsCnt">{chips}</p>
      </S.ChipsRow>
    </S.Container>
  )
}

export default PlayerReultCard
