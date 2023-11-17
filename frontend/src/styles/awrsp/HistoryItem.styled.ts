import { styled } from 'styled-components'

export const ItemContainer = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
`

export const Round = styled.p`
  color: #fff;
  text-align: center;
  text-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  font-size: 16px;
  font-style: normal;
  font-weight: 700;
  line-height: normal;
  letter-spacing: -0.24px;
`

export const Result = styled.p`
  text-align: center;
  text-shadow: 2px 2px 6px rgba(255, 255, 255, 0.25);
  font-size: 16px;
  font-style: normal;
  font-weight: 700;
  line-height: normal;
  background: linear-gradient(180deg, #58a8ff 0%, #fff 50%, #58a8ff 100%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
`

export const CardList = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 4px;
`

export const RspCard = styled.img`
  width: 42px;
  height: 60px;
`
