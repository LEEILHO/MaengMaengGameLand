import * as S from '@styles/user/RankList.styled'
import RankItem from './RankItem'

const RankList = () => {
  return (
    <S.RankListContainer>
      <S.TitleRow>
        <S.RankTitle>등수</S.RankTitle>
        <S.GameCategoryTitle>게임 종류</S.GameCategoryTitle>
      </S.TitleRow>
      {Array(20)
        .fill(0)
        .map(() => (
          <RankItem />
        ))}
    </S.RankListContainer>
  )
}

export default RankList
