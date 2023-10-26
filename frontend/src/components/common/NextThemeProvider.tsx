'use client'

import GlobalStyle from '@styles/globalstyle'
import theme from '@styles/theme'
import { useEffect } from 'react'
import { ThemeProvider } from 'styled-components'

const NextThemeProvider = ({ children }: { children: React.ReactNode }) => {
  let mobile = true

  useEffect(() => {
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
        // 가로 모드에 대한 처리
        mobile = isIos()
      }
    }
    window.addEventListener('orientationchange', handleOrientationChange)
    return () => {
      window.removeEventListener('orientationchange', handleOrientationChange)
    }
  }, [])

  return (
    <ThemeProvider theme={theme}>
      <GlobalStyle isMobile={mobile} />
      {children}
    </ThemeProvider>
  )
}

export default NextThemeProvider
