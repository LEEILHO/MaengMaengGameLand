'use client'

import * as S from '@styles/index/Index.styled'
import useA2HS from '@hooks/useA2HS'
import { useRouter } from 'next/navigation'
import { styled } from 'styled-components'
import { useEffect } from 'react'
import { images } from '@constants/images'

export default function Home() {
  const router = useRouter()
  const { deferredPrompt, installApp, clearPrompt } = useA2HS()
  const isDownload = deferredPrompt ? true : false

  useEffect(() => {
    console.log(deferredPrompt)
  }, [deferredPrompt])

  return (
    <>
      <S.IndexContainer>
        <S.IntroSection>
          <S.TeamName>덜 지니어스</S.TeamName>
          <S.AppName>맹맹게임랜드</S.AppName>
          <S.AppDiscription>꿈과 환상의 나라로 어서오세요!</S.AppDiscription>
          <S.DownLoadButton $isDownload={isDownload}>
            <img
              src={
                isDownload ? images.index.icon : images.index.donwloadComplete
              }
              alt="icon"
            />
            <p>{deferredPrompt ? '다운로드' : '다운로드 완료'}</p>
          </S.DownLoadButton>
        </S.IntroSection>
        <S.GameDiscriptionSection></S.GameDiscriptionSection>
        {/* {deferredPrompt && (
          <div>
            <button onClick={clearPrompt}>취소</button>
            <button onClick={installApp}>홈 화면에 추가</button>
          </div>
        )} */}
      </S.IndexContainer>
    </>
  )
}
