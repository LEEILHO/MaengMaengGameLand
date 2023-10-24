'use client'

import CButton from '@components/common/clients/CButton'
import { styled } from 'styled-components'

const Test = () => {
  return (
    <StyledTestContainer>
      <p>Test</p>
      <CButton
        radius={36}
        backgroundColor="rgba(112, 0, 255, 1)"
        text="테스트 버튼"
        fontSize={16}
        color="white"
      ></CButton>
    </StyledTestContainer>
  )
}

export default Test

const StyledTestContainer = styled.div`
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
`
