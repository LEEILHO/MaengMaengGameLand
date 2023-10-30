import { styled } from 'styled-components'

export const RoomItemContainer = styled.div`
  display: flex;
  flex-basis: 45%;
  flex-direction: column;
  border-radius: 20px;
  border: 1px solid #5d5555;
  background: rgba(109, 109, 109, 0.4);
  width: 342px;
  height: 100px;
`

export const Title = styled.h6`
  font-size: 16px;
  color: white;
  font-weight: 500;
  margin-top: 16px;
  padding: 0 16px;
`

export const BottomRow = styled.div`
  display: flex;
  justify-content: space-between;
  padding: 0 12px;
  margin-top: auto;
  margin-bottom: 16px;
  align-items: center;
`

export const NumberOfPeople = styled.p`
  font-size: 14px;
  color: white;
  font-weight: 500;
`
