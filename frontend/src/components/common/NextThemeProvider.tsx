'use client'

import GlobalStyle from '@styles/globalstyle'
import theme from '@styles/theme'
import { useEffect, useState } from 'react'
import { ThemeProvider } from 'styled-components'

const NextThemeProvider = ({ children }: { children: React.ReactNode }) => {
  const [isMobile, setIsMobile] = useState(false)

  useEffect(() => {
    // ios기반 모델 가로모드 설정
    function isIos() {
      if (typeof window !== 'undefined') {
        var user = window.navigator.userAgent
        return (
          user.indexOf('iPhone') > -1 ||
          // user.indexOf('Android') > -1 ||
          user.indexOf('iPad') > -1 ||
          user.indexOf('iPod') > -1
        )
      }
      return false
    }
    // 사파리에서 가로모드 설정
    function handleOrientationChange() {
      if (window.orientation === 0 || window.orientation === -180) {
        // 모바일 브라우저에 맞춰 높이 조절
        const vh = window.innerHeight * 0.01
        const vw = window.innerWidth * 0.01
        document.documentElement.style.setProperty('--vh', `${vh}px`)
        document.documentElement.style.setProperty('--vw', `${vw}px`)

        // 가로 모드에 대한 처리
        setIsMobile(isIos())
        console.log('맹', isIos())
        // mobile = true
      }
      if (window.orientation === 90 || window.orientation === -90) {
        // 모바일 브라우저에 맞춰 높이 조절
        const vh = window.innerHeight * 0.01
        const vw = window.innerWidth * 0.01
        document.documentElement.style.setProperty('--vh', `${vh}px`)
        document.documentElement.style.setProperty('--vw', `${vw}px`)

        // 가로 모드에 대한 처리
        setIsMobile(false)
        // mobile = true
      }
    }
    handleOrientationChange()
    window.addEventListener('orientationchange', handleOrientationChange)

    return () => {
      window.removeEventListener('orientationchange', handleOrientationChange)
    }
  }, [])

  return (
    <ThemeProvider theme={theme}>
      <GlobalStyle isMobile={false} />
      {isMobile && (
        <div
          style={{
            position: 'absolute',
            zIndex: '100',
            width: '100%',
            height: '100%',
            backgroundColor: 'black',
            color: 'white',
            fontSize: '32px',
            display: 'flex',
            alignItems: 'center',
            textAlign: 'center',
            paddingLeft: '20px',
            paddingRight: '20px',
            boxSizing: 'border-box',
            fontWeight: 'bold',
            lineHeight: '1.4',
          }}
        >
          저희 서비스는 가로모드에 최적화되어있습니다
        </div>
      )}
      {children}
    </ThemeProvider>
  )
}

export default NextThemeProvider
