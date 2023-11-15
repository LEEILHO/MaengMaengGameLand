import { images } from '@constants/images'
import { css, styled } from 'styled-components'

type DownLoadProps = {
  $isDownload: boolean
}

export const IndexContainer = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
`

export const IntroSection = styled.section`
  background-image: url(${images.index.background});
  background-position: center;
  background-size: cover;
  height: 478px;
  display: flex;
  flex-direction: column;
  color: white;
  padding: 32px 28px;
  font-weight: 500;
`

export const GameDiscriptionSection = styled.section`
  height: 800px;
`

export const WatchGameDiscriptionSection = styled.section`
  height: 800px;
`

export const IosGuideDiscriptionSection = styled.section`
  height: fit-content;
  background-color: #f6f7f8;
  width: 100%;
  display: flex;
  flex-direction: column;
  padding: 80px 12px;
`

export const GuideTitle = styled.h5`
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 40px;
  text-align: center;
`

export const GuideRow = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
  align-items: center;
  justify-content: center;
  margin-top: 80px;
`

export const GuideImage = styled.img`
  width: 245px;
`

export const GuideDiscription = styled.p`
  font-size: 14px;
  font-weight: 500;
  margin-top: 40px;
  white-space: pre-wrap;
  line-height: 140%;
`

export const TeamName = styled.h5`
  font-size: 16px;
  font-weight: 700;
`

export const AppName = styled.h1`
  font-size: 48px;
  font-weight: 700;
  margin-top: 192px;
`

export const AppDiscription = styled.h2`
  font-size: 16px;
  margin-top: 20px;
`

export const DownLoadButton = styled.button<DownLoadProps>`
  margin-top: 32px;
  border-radius: 4px;
  border: 1px solid rgba(255, 255, 255, 0.5);
  background: rgba(255, 255, 255, 0.25);
  font-size: 16px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  height: 50px;
  width: fit-content;
  color: white;

  ${(props) =>
    !props.$isDownload &&
    css`
      color: #dadada;
    `}

  img {
    width: 36px;
    height: 36px;
    margin-left: 8px;
    margin-right: 8px;
  }

  p {
    margin-right: 12px;
  }
`
