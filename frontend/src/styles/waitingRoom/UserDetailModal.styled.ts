import { styled } from 'styled-components'

export const UserDetailModalContainer = styled.div`
  width: 397px;
  height: 209px;
  background-color: white;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  position: relative;
`

export const UserProfile = styled.img`
  width: 60px;
  height: 60px;
  border-radius: 50px;
  position: absolute;
  top: 20px;
  left: 30px;
`

export const UserDetail = styled.table`
  display: flex;
  flex-direction: column;
  gap: 16px;
  font-size: 18px;
  font-weight: 700;
  width: 150px;
  margin-top: 32px;

  tr {
    display: flex;
    justify-content: space-between;
  }

  .info {
    font-weight: 500;
  }
`
export const BottomButtons = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
`

export const CloseButton = styled.img`
  position: absolute;
  top: 12px;
  right: 12px;
  width: 28px;
  height: 28px;
`
