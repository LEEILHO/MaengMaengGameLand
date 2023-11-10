import { jwacRoundResultState } from '@atom/jwacAtom'
import { images } from '@constants/images'
import * as S from '@styles/gameRoom/jwac/JWACRoundResultDisplay.styled'
import { JewelryType, RoundResultType } from '@type/gameRoom/jwac.type'
import { useRecoilValue } from 'recoil'

type Props = {
  jewely: JewelryType
  socre: number
  roundResult: RoundResultType | null
}

const JWACRoundResultDisplay = ({ jewely, socre, roundResult }: Props) => {
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
    <S.RoundResultDisplayContainer>
      <S.JewelyDataRow>
        <S.Jewely src={getJewelySrc(jewely)} alt="" />
        <S.JewelyName $type={jewely}>{getJewelyName(jewely)}</S.JewelyName>
        {jewely !== 'SPECIAL' && (
          <S.JewelyCost $type={jewely}>{`${socre}점`}</S.JewelyCost>
        )}
      </S.JewelyDataRow>
      <S.DisplayRow>
        <S.SuccessfulBidder>낙찰자</S.SuccessfulBidder>
        <S.SuccessfulBidderName>
          {roundResult?.mostBidder}
        </S.SuccessfulBidderName>
        {jewely !== 'SPECIAL' && (
          <S.SuccessfulBidderScore>
            {`(+${roundResult?.jewelryScore})`}
          </S.SuccessfulBidderScore>
        )}
      </S.DisplayRow>
      <S.DisplayRow>
        <S.LowestPriceBidder>최저 금액 입찰자</S.LowestPriceBidder>
        <S.LowestPriceBidderName>
          {roundResult?.leastBidder}
        </S.LowestPriceBidderName>
        {jewely !== 'SPECIAL' && (
          <S.LowestPriceBidderScore>(-1)</S.LowestPriceBidderScore>
        )}
      </S.DisplayRow>
    </S.RoundResultDisplayContainer>
  )
}

export default JWACRoundResultDisplay
