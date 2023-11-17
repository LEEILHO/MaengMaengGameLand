import { styled } from 'styled-components'

export const Container = styled.div`
  width: 125px;
  height: 90px;
  border-radius: 12px;
  background: #d9d9d9;

  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
`

export const Nickname = styled.p`
  width: 100%;
  text-align: center;
  font-size: 16px;
  font-style: normal;
  font-weight: 500;
  line-height: normal;

  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
`

export const Rank = styled.p`
  text-align: center;
  font-size: 16px;
  font-style: normal;
  font-weight: 700;
  line-height: normal;
`

export const Score = styled.p`
  text-align: center;
  font-size: 12px;
  font-style: normal;
  font-weight: 500;
  line-height: normal;
`
