'use client'

import CButton from '@components/common/clients/CButton'
import Header from '@components/common/clients/Header'
import Timer from '@components/common/clients/Timer'
import withAuth from '@components/hoc/client/PrivateRoute'
import { useRouter } from 'next/navigation'
import { styled } from 'styled-components'

const Test = () => {
  const router = useRouter()

  return (
    <StyledTestContainer>
      <Header viewFriend={true} viewSetting={true} />
      <CButton
        radius={36}
        backgroundColor="rgba(112, 0, 255, 1)"
        text="테스트 버튼"
        fontSize={16}
        color="white"
        onClick={() => router.push('/')}
      ></CButton>
      <Timer size="150" fontSize="24" time={10} />
    </StyledTestContainer>
  )
}

export default withAuth(Test)

const StyledTestContainer = styled.div`
  position: fixed;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: purple;
`
