'use client'

import GlobalStyle from '@styles/globalstyle'
import theme from '@styles/theme'
import { useEffect } from 'react'
import { ThemeProvider } from 'styled-components'

const NextThemeProvider = ({ children }: { children: React.ReactNode }) => {
  useEffect(() => {
    if (window.screen && window.screen.orientation) {
      window.screen.orientation.lock('landscape').then(
        function success() {
          console.log('화면이 가로 모드로 고정되었습니다.')
        },
        function error(err) {
          console.error('화면 회전을 고정하지 못했습니다: ', err)
        },
      )
    }
  }, []) // []를 빈 배열로 전달하여 한 번만 실행되도록 설정

  return (
    <ThemeProvider theme={theme}>
      <GlobalStyle />
      {children}
    </ThemeProvider>
  )
}

export default NextThemeProvider
