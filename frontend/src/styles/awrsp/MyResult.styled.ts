import { styled } from 'styled-components'

export const MyResultContainer = styled.div`
  width: 100%;
  height: 100%;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  gap: 24px;
`

export const WinCount = styled.h2`
  text-align: center;
  text-shadow: 2px 2px 6px rgba(255, 255, 255, 0.25);
  font-size: 54px;
  font-weight: 700;
  line-height: normal;

  background: linear-gradient(180deg, #58a8ff 0%, #fff 50%, #58a8ff 100%);
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
`

export const CardList = styled.div`
  border-radius: 8px;
  background-color: rgba(217, 217, 217, 0.5);

  padding: 20px 10px;

  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
`

export const RspCard = styled.img`
  width: 64px;
  height: 96px;

  @media screen and (max-width: 700px) {
    width: 48px;
    height: 80px;
  }
`
