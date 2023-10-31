import { images } from '@constants/images'
import * as S from '@styles/home/GameCard.styled'

type Props = {
  backGroundUrl: string
  name: string
  onClick: () => void
}

const GameCard = ({ backGroundUrl, name, onClick }: Props) => {
  return (
    <>
      <S.GameCardContainer>
        <S.GameImageContainer $backGroundUrl={backGroundUrl} onClick={onClick}>
          <S.DiscriptionIcon src={images.home.discription} alt="설명" />
        </S.GameImageContainer>
        <S.GameName>{name}</S.GameName>
      </S.GameCardContainer>
    </>
  )
}

export default GameCard
