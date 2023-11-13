import { styled } from 'styled-components'

export const HistoryModalContainer = styled.div`
  width: 504px;
  height: 252px;
  border-radius: 8px;
  background: rgba(79, 79, 79, 0.85);
  box-shadow: 4px 8px 10px 0px rgba(0, 0, 0, 0.25);

  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;

  padding: 20px;
`

export const Title = styled.h6`
  color: #fff;
  text-align: center;
  text-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  font-size: 20px;
  font-weight: 700;
  line-height: normal;
  letter-spacing: -0.4px;
`
export const Content = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 80%;
`

export const Nav = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 50%;

  p {
    color: #fff;
    text-align: center;
    font-size: 16px;
    font-style: normal;
    font-weight: 700;
    line-height: normal;
    letter-spacing: -0.32px;
  }
`
