import { styled } from 'styled-components'

export const PlayerCardContainer = styled.div`
  width: 160px;
  height: 201px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  padding: 16px;

  color: #000;
  text-align: center;
  font-size: 20px;
  font-weight: 700;
  line-height: normal;

  border-radius: 12px;
  background: rgba(228, 241, 255, 0.75);

  @media screen and (max-width: 700px) {
    width: 120px;
    height: 160px;

    font-size: 16px;
  }
`

export const NicknameP = styled.p``

export const ChipsRow = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;

  .chip {
    width: 34px;
    height: 30px;
  }

  .mul {
    width: 16px;
    height: 16px;
  }

  .chipsCnt {
  }

  @media screen and (max-width: 700px) {
    .chip {
      width: 28px;
      height: 28px;
    }
  }
`

export const WeightDisplay = styled.p`
  width: 128px;
  height: 91px;
  background: linear-gradient(
      137deg,
      rgba(0, 0, 0, 0.3) 45.88%,
      rgba(255, 255, 255, 0.1) 60.41%,
      rgba(0, 0, 0, 0.3) 74.5%
    ),
    #2a2a2a;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;

  @media screen and (max-width: 700px) {
    width: 100px;
    height: 75px;
  }
`
