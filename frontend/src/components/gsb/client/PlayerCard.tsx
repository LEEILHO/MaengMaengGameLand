'use client'

import { images } from '@constants/images'
import * as S from '@styles/gsb/PlayerCard.styled'

type Props = {
  nickname: string
  chipsPlayerHas: number
  weight: number
}

const PlayerCard = ({ nickname, chipsPlayerHas, weight }: Props) => {
  return (
    <S.PlayerCardContainer>
      <S.NicknameP>{nickname}</S.NicknameP>
      <S.ChipsRow>
        <img className="chip" src={images.gsb.chip} alt="chip" />
        <img className="mul" src={images.gsb.mul} alt="곱하기" />
        <p className="chipsCnt">{chipsPlayerHas}</p>
      </S.ChipsRow>
      <S.WeightDisplay>{weight}g</S.WeightDisplay>
    </S.PlayerCardContainer>
  )
}

export default PlayerCard
