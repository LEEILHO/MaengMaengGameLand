'use client'

import * as S from '@styles/index/Index.styled'
import useA2HS from '@hooks/useA2HS'
import { useEffect, useState, useRef } from 'react'
import { images } from '@constants/images'
import { detectIosDevice } from '@utils/common/mobile'

export default function Home() {
  const { deferredPrompt, installApp, clearPrompt } = useA2HS()
  const iosGuideRef = useRef<HTMLSelectElement | null>(null)
  const [isIos, setIsIos] = useState(false)
  const isDownload = deferredPrompt ? true : false

  const handleDownload = () => {
    if (isDownload) {
      installApp()
    }
  }

  const handleIosDownload = () => {
    iosGuideRef.current?.scrollIntoView({ behavior: 'smooth' })
  }

  useEffect(() => {
    if (typeof window !== 'undefined') {
      document.body.style.overflow = 'auto'
      setIsIos(detectIosDevice(window.navigator.userAgent))
    }
  }, [])

  return (
    <>
      <S.IndexContainer>
        <S.IntroSection>
          <S.TeamName>덜 지니어스</S.TeamName>
          <S.AppName>맹맹게임랜드</S.AppName>
          <S.AppDiscription>꿈과 환상의 나라로 어서오세요!</S.AppDiscription>
          {isIos ? (
            <S.DownLoadButton $isDownload={true} onClick={handleIosDownload}>
              <img
                src={images.index.appleIcon}
                alt="icon"
                style={{ width: '28px', height: '28px' }}
              />
              <p>ios 다운로드</p>
            </S.DownLoadButton>
          ) : (
            <S.DownLoadButton $isDownload={isDownload} onClick={handleDownload}>
              <img
                src={
                  isDownload ? images.index.icon : images.index.donwloadComplete
                }
                alt="icon"
              />
              <p>{deferredPrompt ? '다운로드' : '다운로드 완료'}</p>
            </S.DownLoadButton>
          )}
        </S.IntroSection>
        <S.GameDiscriptionSection></S.GameDiscriptionSection>
        {isIos && (
          <S.IosGuideDiscriptionSection ref={iosGuideRef}>
            <S.GuideTitle>아이폰 다운로드 가이드</S.GuideTitle>
            <S.GuideRow>
              <S.GuideImage
                src={images.index.iosGuide1}
                alt="아이폰 설치 가이드1"
              />
              <S.GuideDiscription>
                먼저 하단바의 중앙에 위치한 버튼을 클릭해주세요.
              </S.GuideDiscription>
            </S.GuideRow>
            <S.GuideRow>
              <S.GuideImage
                src={images.index.iosGuide2}
                alt="아이폰 설치 가이드2"
              />
              <S.GuideDiscription>
                이후 올라오는 창에서 홈 화면에 추가를 클릭해주세요.
              </S.GuideDiscription>
            </S.GuideRow>
            <S.GuideRow>
              <S.GuideImage
                src={images.index.iosGuide3}
                alt="아이폰 설치 가이드3"
              />
              <S.GuideDiscription>
                이후 추가버튼을 클릭해 앱을 설치해줍니다.
              </S.GuideDiscription>
            </S.GuideRow>
            <S.GuideRow>
              <S.GuideImage
                src={images.index.iosGuide4}
                alt="아이폰 설치 가이드4"
              />
              <S.GuideDiscription>
                {`홈화면에 설치된 앱을 이용할 수 있습니다!
                  맹맹 게임랜드로 출발 🚀`}
              </S.GuideDiscription>
            </S.GuideRow>
          </S.IosGuideDiscriptionSection>
        )}
      </S.IndexContainer>
    </>
  )
}
