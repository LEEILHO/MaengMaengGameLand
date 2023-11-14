import { styled } from 'styled-components'

export const RankListContainer = styled.div`
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  flex: 1;
  margin: 20px 40px;
  border-radius: 8px;
  background: rgba(0, 0, 0, 0.5);
  color: white;
`

export const TitleRow = styled.div`
  display: flex;
  width: 100%;
  font-size: 18px;
  font-weight: 700;
  min-height: 60px;
  padding-left: 20px;
`

export const RankTitle = styled.h5`
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
`

export const GameCategoryTitle = styled.h5`
  flex: 3;
  display: flex;
  align-items: center;
  justify-content: center;
`
