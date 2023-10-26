'use client'

import GlobalStyle from '@styles/globalstyle'
import theme from '@styles/theme'
import { useEffect } from 'react'
import { ThemeProvider } from 'styled-components'

const NextThemeProvider = ({ children }: { children: React.ReactNode }) => {
  let mobile = false

  useEffect(() => {
    function isMobile() {
      if (typeof window !== 'undefined') {
        var user = window.navigator.userAgent
        return (
          user.indexOf('iPhone') > -1 ||
          user.indexOf('Android') > -1 ||
          user.indexOf('iPad') > -1 ||
          user.indexOf('iPod') > -1
        )
      }
      return false
    }

    mobile = isMobile()
  }, [])

  return (
    <ThemeProvider theme={theme}>
      <GlobalStyle isMobile={mobile} />
      {children}
    </ThemeProvider>
  )
}

export default NextThemeProvider
