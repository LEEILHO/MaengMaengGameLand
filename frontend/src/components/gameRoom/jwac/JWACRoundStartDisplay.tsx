import { images } from '@constants/images'
import * as S from '@styles/gameRoom/jwac/JWACRoundStartDisplay.styled'
import { JewelryType } from '@type/gameRoom/jwac.type'

type Props = {
  jewely: JewelryType
  socre: number
}

const JWACRoundStartDisplay = ({ jewely, socre }: Props) => {
  const getJewelyName = (jewely: JewelryType) => {
    if (jewely === 'DIAMOND') return '다이아몬드'
    if (jewely === 'RUBY') return '루비'
    if (jewely === 'EMERALD') return '에메랄드'
    if (jewely === 'SAPPHIRE') return '사파이어'
    if (jewely === 'SPECIAL') return '보석 정보 확인서'
  }

  const getJewelySrc = (jewely: JewelryType) => {
    if (jewely === 'DIAMOND') return images.gameRoom.jwac.diamond
    if (jewely === 'RUBY') return images.gameRoom.jwac.ruby
    if (jewely === 'EMERALD') return images.gameRoom.jwac.emerald
    if (jewely === 'SAPPHIRE') return images.gameRoom.jwac.sapphire
    if (jewely === 'SPECIAL') return images.gameRoom.jwac.jewelryInfomation
  }

  return (
    <S.RoundStartDisplayContainer>
      <S.Jewely src={getJewelySrc(jewely)} alt="" />
      <S.JewelyName $type={jewely}>{getJewelyName(jewely)}</S.JewelyName>
      {jewely !== 'SPECIAL' && (
        <S.JewelyCost $type={jewely}>{`${socre}점`}</S.JewelyCost>
      )}
    </S.RoundStartDisplayContainer>
  )
}

export default JWACRoundStartDisplay
