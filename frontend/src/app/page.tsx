'use client'

import { useRouter } from 'next/navigation'
import { styled } from 'styled-components'

export default function Home() {
  const router = useRouter()
  return (
    <>
      <HomeContainer onClick={() => router.push('/home')}>
        Hello World!
      </HomeContainer>
    </>
  )
}

const HomeContainer = styled.div`
  font-size: 80px;
`
