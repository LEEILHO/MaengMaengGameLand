import { styled } from 'styled-components'

interface ResultProps {
  result: string
}

export const Container = styled.div<ResultProps>`
  width: 180px;
  height: 210px;

  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;

  border-radius: 12px;
  background-color: ${(props) =>
    props.result === 'lose'
      ? 'rgba(126, 126, 126, 0.75)'
      : 'rgba(228, 241, 255, 0.75)'};
  scale: ${(props) => (props.result === 'win' ? 'calc(1.1)' : '')};
`

export const ResultTitle = styled.h6<ResultProps>`
  text-transform: uppercase;
  text-align: center;
  text-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
  font-size: ${(props) => (props.result === 'win' ? '32px' : '28px')};
  font-style: normal;
  font-weight: 700;
  line-height: normal;
  background: ${(props) =>
    props.result === 'lose'
      ? 'linear-gradient(180deg, #000 0%, #404040 50%, #000 100%)'
      : 'linear-gradient(180deg, #7000FF 0%, #C292FF 52.08%, #7000FF 100%)'};
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
`

export const ProfileImage = styled.img`
  width: 56px;
  height: 56px;
  border-radius: 50%;
`

export const Nickname = styled.p`
  color: #000;
  text-align: center;
  font-size: 20px;
  font-style: normal;
  font-weight: 700;
  line-height: normal;
`

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
    font-weight: 700;
  }

  @media screen and (max-width: 700px) {
    .chip {
      width: 28px;
      height: 28px;
    }
  }
`
