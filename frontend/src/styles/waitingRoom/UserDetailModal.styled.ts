import { styled } from 'styled-components'

export const UserDetailModalContainer = styled.div`
  width: 397px;
  height: 209px;
  background-color: white;
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
`

export const UserProfile = styled.img`
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
`

export const UserDetail = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  font-size: 18px;
  font-weight: 700;
`

export const Nickname = styled.p`
  text-align: center;
  display: flex;
  align-items: center;
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
