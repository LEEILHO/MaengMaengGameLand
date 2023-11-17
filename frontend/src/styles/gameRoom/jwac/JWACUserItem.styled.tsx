import { styled } from 'styled-components'

export const JWACUserItemContainer = styled.div`
  display: flex;
  flex-basis: 45%;
  align-items: center;
  border-radius: 8px;
  background: rgba(217, 217, 217, 0.5);
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
  height: 40px;
  /* max-height: 42px; */
`

export const Empty = styled.div`
  flex-basis: 45%;
  height: 40px;
`

export const UserProfile = styled.img`
  margin-left: 6px;
  border-radius: 50%;
  width: 32px;
  height: 32px;
  object-fit: cover;
`

export const Username = styled.p`
  font-size: 14px;
  font-weight: 500;
  color: #ffea29;
  margin-left: 8px;
  margin-right: auto;
`

export const UserScore = styled.p`
  font-size: 14px;
  font-weight: 500;
  color: white;
  margin-right: 8px;
`
