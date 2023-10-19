import type { Metadata } from 'next'
import './globals.css'
import RecoilRootProvider from '@components/common/RecoilRootProvider'
import NextThemeProvider from '@components/common/NextThemeProvider'

export const metadata: Metadata = {
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
    title: '맹맹마블',
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
        <NextThemeProvider>
          <RecoilRootProvider>{children}</RecoilRootProvider>
        </NextThemeProvider>
      </body>
    </html>
  )
}
