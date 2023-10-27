import { images } from '@constants/images'
import * as S from '@styles/home/GameCard.styled'

type Props = {
  backGroundUrl: string
  name: string
}

const GameCard = ({ backGroundUrl, name }: Props) => {
  return (
    <>
      <S.GameCardContainer>
        <S.GameImageContainer $backGroundUrl={backGroundUrl}>
          <S.DiscriptionIcon src={images.home.discription} alt="설명" />
        </S.GameImageContainer>
        <S.GameName>{name}</S.GameName>
      </S.GameCardContainer>
    </>
  )
}

export default GameCard
