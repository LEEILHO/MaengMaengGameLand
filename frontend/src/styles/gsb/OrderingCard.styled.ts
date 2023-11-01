import { styled } from 'styled-components'

export const OrderingCardContainer = styled.div`
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.75);
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1;

  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`

export const DisplayBoard = styled.div`
  width: 60%;
  padding: 16px 54px;
  border-radius: 12px;
  background: rgba(228, 241, 255, 0.75);

  position: fixed;
  top: 20px;
  left: 50%;
  transform: translate(-50%, 0%);

  text-align: center;
  font-size: 18px;
  font-weight: 700;
  line-height: normal;
`

export const CardList = styled.div``
