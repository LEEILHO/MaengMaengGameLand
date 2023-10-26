import type { Metadata } from 'next'
import RecoilRootProvider from '@components/common/RecoilRootProvider'
import NextThemeProvider from '@components/common/NextThemeProvider'
import RootStyleRegistry from '@styles/RootStyleRegistry'

export const metadata: Metadata = {
  viewport: {
    width: 'device-width',
    initialScale: 1,
    minimumScale: 1,
    maximumScale: 1,
    userScalable: false,
  },
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
        <RootStyleRegistry>
          <NextThemeProvider>
            <RecoilRootProvider>{children}</RecoilRootProvider>
          </NextThemeProvider>
        </RootStyleRegistry>
      </body>
    </html>
  )
}
