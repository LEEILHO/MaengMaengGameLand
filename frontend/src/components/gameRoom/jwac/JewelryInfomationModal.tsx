import { jwacJewelryItemState } from '@atom/jwacAtom'
import * as S from '@styles/gameRoom/jwac/JewelryInfomationModal.styled'
import { useRecoilValue } from 'recoil'

const JewelryInfomationModal = () => {
  const jewelryInfo = useRecoilValue(jwacJewelryItemState)

  return (
    <S.JewerlyInfomationContainer>
      <S.Title>보석 정보 확인권</S.Title>
      <S.Row>
        <S.JewerlyName>루비 : </S.JewerlyName>
        <S.JewerlyCount>{`${jewelryInfo?.RUBY}개`}</S.JewerlyCount>
      </S.Row>
      <S.Row>
        <S.JewerlyName>사파이어 : </S.JewerlyName>
        <S.JewerlyCount>{`${jewelryInfo?.SAPPHIRE}개`}</S.JewerlyCount>
      </S.Row>
      <S.Row>
        <S.JewerlyName>에메랄드 : </S.JewerlyName>
        <S.JewerlyCount>{`${jewelryInfo?.EMERALD}개`}</S.JewerlyCount>
      </S.Row>
      <S.Row>
        <S.JewerlyName>다이아 : </S.JewerlyName>
        <S.JewerlyCount>{`${jewelryInfo?.DIAMOND}개`}</S.JewerlyCount>
      </S.Row>
    </S.JewerlyInfomationContainer>
  )
}

export default JewelryInfomationModal
