'use client'

import { images } from '@constants/images'
import * as S from '@styles/gsb/PlayerCard.styled'
import { PlayerInfoType } from '@type/gsb/gsb.type'

type Props = {
  player: PlayerInfoType | null
}

const PlayerCard = ({ player }: Props) => {
  if (!player) return

  return (
    <S.PlayerCardContainer>
      <S.NicknameP>{player.nickname}</S.NicknameP>
      <S.ChipsRow>
        <img className="chip" src={images.gsb.chip} alt="chip" />
        <img className="mul" src={images.gsb.mul} alt="곱하기" />
        <p className="chipsCnt">{player.currentChips}</p>
      </S.ChipsRow>
      <S.WeightDisplay>{player.currentWeight}g</S.WeightDisplay>
    </S.PlayerCardContainer>
  )
}

export default PlayerCard
