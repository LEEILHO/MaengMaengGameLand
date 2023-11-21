import { styled } from 'styled-components'

export const BettingContainer = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: fixed;
  bottom: calc(0px + env(keyboard-inset-height, 0));
  padding: 0px 40px 20px 40px;
`

export const BettingInputContainer = styled.div`
  /* width: 150px; */
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  border-bottom: 3px solid white;
  padding: 8px 8px 8px 0px;

  img {
    width: 30px;
    height: 30px;
  }

  @media screen and (max-width: 700px) {
    img {
      width: 28px;
      height: 28px;
    }
  }
`

export const BettingInput = styled.input`
  width: 120px;
  background: none;
  text-align: center;
  font-size: 24px;
  font-family: 'kbo-dia';
  font-weight: 700;
  color: white;

  @media screen and (max-width: 700px) {
    width: 100px;
    font-size: 20px;
  }
`

export const BettingStatusContainer = styled.div`
  width: 100%;
  height: 201px;
  display: flex;
  align-items: center;
  justify-content: space-around;

  position: absolute;
  top: 0;

  @media screen and (max-width: 700px) {
    height: 160px;
  }
`

export const Chips = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 8px;

  img {
    width: 40px;
    height: 50px;
  }

  p {
    color: white;
    font-weight: 700;
    font-size: 20px;
  }
`
