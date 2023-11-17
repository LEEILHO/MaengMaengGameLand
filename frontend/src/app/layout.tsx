import type { Metadata } from 'next'
import RecoilRootProvider from '@components/common/RecoilRootProvider'
import NextThemeProvider from '@components/common/NextThemeProvider'
import RootStyleRegistry from '@styles/StyledComponentsRegistry'
import StyledComponentsRegistry from '@styles/StyledComponentsRegistry'
import BGMProvider from '@components/common/BGMProvider'

export const metadata: Metadata = {
  viewport:
    'minimum-scale=1, initial-scale=1, width=device-width, shrink-to-fit=no, viewport-fit=cover user-scalable=no',
  title: '맹맹게임랜드',
  description: '맹맹게임랜드에 오신걸 환영합니다.',
  keywords: ['맹맹게임랜드', 'Next.js', 'React', 'JavaScript', 'Game'],
  authors: [{ name: 'Ksg' }],
  robots: 'follow, index',
  applicationName: '맹맹게임랜드',
  manifest: '/manifest.json',
  themeColor: '#ffffff',
  verification: { google: 'l9gVFrGOi6xZXLXDfhrva_lqe8XRAiCHLk0jnqEE4bE' },
  icons: {
    apple: '/icons/apple-touch-icon.png',
    other: [
      {
        url: '/icons/iphone-splash.png',
        media:
          'screen and (device-width: 375px) and (device-height: 667px) and (-webkit-device-pixel-ratio: 2) and (orientation: portrait)',
        rel: 'apple-touch-startup-image',
      },
    ],
  },
  appleWebApp: {
    capable: true,
    title: '맹맹게임랜드',
    statusBarStyle: 'black-translucent',
  },
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html>
      <body>
        <StyledComponentsRegistry>
          <NextThemeProvider>
            <RecoilRootProvider>
              <BGMProvider>{children}</BGMProvider>
              <div id="portal"></div>
            </RecoilRootProvider>
          </NextThemeProvider>
        </StyledComponentsRegistry>
      </body>
    </html>
  )
}
