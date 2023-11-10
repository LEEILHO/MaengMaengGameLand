import type { Metadata } from 'next'
import RecoilRootProvider from '@components/common/RecoilRootProvider'
import NextThemeProvider from '@components/common/NextThemeProvider'
import RootStyleRegistry from '@styles/StyledComponentsRegistry'
import StyledComponentsRegistry from '@styles/StyledComponentsRegistry'
import { sounds } from '@constants/sounds'

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
  icons: { apple: '/icons/apple-touch-icon.png' },
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
              <audio src={sounds.lobby.main} autoPlay loop></audio>
              {children}
              <div id="portal"></div>
            </RecoilRootProvider>
          </NextThemeProvider>
        </StyledComponentsRegistry>
      </body>
    </html>
  )
}
