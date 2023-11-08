'use client'

import useA2HS from '@hooks/useA2HS'
import { useRouter } from 'next/navigation'
import { styled } from 'styled-components'
import { useEffect } from 'react'

export default function Home() {
  const router = useRouter()
  const { deferredPrompt, installApp, clearPrompt } = useA2HS()

  useEffect(() => {
    console.log(deferredPrompt)
  }, [deferredPrompt])

  return (
    <>
      <HomeContainer onClick={() => router.replace('/home')}>
        Hello World!
        {deferredPrompt && (
          <div>
            <button onClick={clearPrompt}>취소</button>
            <button onClick={installApp}>홈 화면에 추가</button>
          </div>
        )}
      </HomeContainer>
    </>
  )
}

const HomeContainer = styled.div`
  font-size: 80px;
`
