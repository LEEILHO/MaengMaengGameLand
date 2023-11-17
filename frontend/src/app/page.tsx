'use client'

import * as S from '@styles/index/Index.styled'
import useA2HS from '@hooks/useA2HS'
import { useEffect, useState, useRef } from 'react'
import { images } from '@constants/images'
import { detectIosDevice } from '@utils/common/mobile'
import { useAnimate, useAnimation } from 'framer-motion'

export default function Home() {
  const { deferredPrompt, installApp, clearPrompt } = useA2HS()
  const iosGuideRef = useRef<HTMLSelectElement | null>(null)
  const [isIos, setIsIos] = useState(false)
  const isDownload = deferredPrompt ? true : false
  const [seletedTab, setSeletedTab] = useState<'GAME' | 'WATCH'>('GAME')
  const [isNavFix, setIsNavFix] = useState(false)
  const animaiton1 = useAnimation()
  const animaiton2 = useAnimation()
  const animaiton3 = useAnimation()
  const animaiton4 = useAnimation()
  const animaiton5 = useAnimation()
  const textAnimation1 = useAnimation()
  const textAnimation2 = useAnimation()
  const textAnimation3 = useAnimation()
  const textAnimation4 = useAnimation()
  const textAnimation5 = useAnimation()

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

  useEffect(() => {
    const handleScroll = () => {
      const scrollPosition = window.scrollY
      const scrollThreshold = 560

      if (scrollPosition > scrollThreshold) {
        setIsNavFix(true)
      } else {
        setIsNavFix(false)
      }
    }

    window.addEventListener('scroll', handleScroll)

    return () => {
      window.removeEventListener('scroll', handleScroll)
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
        <S.GameTabNavigation $isFix={isNavFix}>
          <S.GameDiscriptionTab
            $active={seletedTab === 'GAME'}
            onClick={() => {
              setSeletedTab('GAME')
            }}
          >
            모바일 게임
          </S.GameDiscriptionTab>
          <S.WatchDiscriptionTab
            $active={seletedTab === 'WATCH'}
            onClick={() => {
              setSeletedTab('WATCH')
            }}
          >
            워치 게임
          </S.WatchDiscriptionTab>
        </S.GameTabNavigation>
        {seletedTab === 'GAME' && (
          <S.GameDiscriptionSection $isFix={isNavFix}>
            <S.GameRow>
              <S.GameImage
                src={images.index.gameGuide1}
                alt="게임가이드"
                initial={{ opacity: 0, y: 100 }}
                animate={animaiton1}
                transition={{ duration: 1 }}
                onViewportEnter={() => {
                  animaiton1.start({ opacity: 1, y: 0 })
                }}
              />
              <S.GameDiscription
                initial={{ opacity: 0, y: 100 }}
                animate={textAnimation1}
                transition={{ duration: 1 }}
                onViewportEnter={() => {
                  textAnimation1.start({ opacity: 1, y: 0 })
                }}
              >
                {`맹맹게임랜드에서
다양한 게임을 즐겨보세요.`}
              </S.GameDiscription>
            </S.GameRow>
            <S.GameName>금은동</S.GameName>
            <S.GameRow>
              <S.GameImage
                src={images.index.gameGuide2}
                alt="게임가이드"
                initial={{ opacity: 0, y: 100 }}
                animate={animaiton2}
                transition={{ duration: 1 }}
                onViewportEnter={() => {
                  animaiton2.start({ opacity: 1, y: 0 })
                }}
              />

              <S.GameDiscription
                initial={{ opacity: 0, y: 100 }}
                animate={textAnimation2}
                transition={{ duration: 1 }}
                onViewportEnter={() => {
                  textAnimation2.start({ opacity: 1, y: 0 })
                }}
              >
                {`속고 속이는 심리 게임에서
잔혹하게 베팅해라!`}
              </S.GameDiscription>
            </S.GameRow>
            <S.GameName>무제한 보석 경매</S.GameName>
            <S.GameRow>
              <S.GameImage
                src={images.index.gameGuide3}
                alt="게임가이드"
                initial={{ opacity: 0, y: 100 }}
                animate={animaiton3}
                transition={{ duration: 1 }}
                onViewportEnter={() => {
                  animaiton3.start({ opacity: 1, y: 0 })
                }}
              />
              <S.GameDiscription
                initial={{ opacity: 0, y: 100 }}
                animate={textAnimation3}
                transition={{ duration: 1 }}
                onViewportEnter={() => {
                  textAnimation3.start({ opacity: 1, y: 0 })
                }}
              >
                {`무자비한 보석 경매의 싸움에서
치열하게 살아남아라!`}
              </S.GameDiscription>
            </S.GameRow>
            <S.GameName>전승 가위바위보</S.GameName>
            <S.GameRow>
              <S.GameImage
                src={images.index.gameGuide4}
                alt="게임가이드"
                initial={{ opacity: 0, y: 100 }}
                animate={animaiton4}
                transition={{ duration: 1 }}
                onViewportEnter={() => {
                  animaiton4.start({ opacity: 1, y: 0 })
                }}
              />
              <S.GameDiscription
                initial={{ opacity: 0, y: 100 }}
                animate={textAnimation4}
                transition={{ duration: 1 }}
                onViewportEnter={() => {
                  textAnimation4.start({ opacity: 1, y: 0 })
                }}
              >
                {`미지의 카드 7개를 모두
이기는 조합을 찾아내는 인물은
과연 누구인가?`}
              </S.GameDiscription>
            </S.GameRow>
          </S.GameDiscriptionSection>
        )}
        {seletedTab === 'WATCH' && (
          <S.WatchGameDiscriptionSection $isFix={isNavFix}>
            <S.GameName>맹맹 점프</S.GameName>
            <S.GameRow>
              <S.GameImage
                src={images.index.watchGameGuide}
                alt="맹맹 점프"
                initial={{ opacity: 0, y: 100 }}
                animate={animaiton5}
                transition={{ duration: 1 }}
                onViewportEnter={() => {
                  animaiton5.start({ opacity: 1, y: 0 })
                }}
              />
              <S.GameDiscription
                initial={{ opacity: 0, y: 100 }}
                animate={textAnimation5}
                transition={{ duration: 1 }}
                onViewportEnter={() => {
                  textAnimation5.start({ opacity: 1, y: 0 })
                }}
              >
                {`점프! 점프!
토끼와 함께 달나라로 떠나요!`}
              </S.GameDiscription>
            </S.GameRow>
          </S.WatchGameDiscriptionSection>
        )}
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
