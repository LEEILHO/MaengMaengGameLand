import { styled } from 'styled-components'

export const RoomListContainer = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 100%;
  margin: 0 44px;
`

export const RoomBox = styled.div`
  position: relative;
  display: flex;
  flex-wrap: wrap;
  width: 100%;
  height: 100%;
  border-radius: 20px;
  background: rgba(50, 46, 46, 0.5);
  overflow-y: scroll;
  padding: 20px 0;
  gap: 12px;
  justify-content: center;

  /* 스크롤 바 커스텀 */
  &::-webkit-scrollbar {
    width: 8px; /* 스크롤 바 너비 지정 */
  }
  &::-webkit-scrollbar-track {
    background-color: rgba(72, 66, 87, 1); /* 배경색 설정 */
    border-radius: 5px; /* 흰색 둥근 테두리를 만들기 위해 사용 */
  }
  &::-webkit-scrollbar-thumb {
    background-color: white; /* 스크롤 바 색상 (흰색) 설정 */
    border-radius: 5px; /* 둥근 테두리를 만들기 위해 사용 */
  }
  &::-webkit-scrollbar-thumb:hover {
    background-color: lightgray; /* 마우스 호버 시 색상 변경 */
  }
`

export const TypeButtonRaw = styled.div`
  margin-left: auto;
  display: flex;
  gap: 4px;
`
