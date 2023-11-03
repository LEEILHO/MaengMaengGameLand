import { images } from '@constants/constants'
import { styled } from 'styled-components'

export const LoginRedirect = styled.div`
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;
  background-image: url(${images.login.background});
  background-repeat: no-repeat;
  background-size: cover;

  display: flex;
  justify-content: center;
  align-items: center;
`

export const Loading = styled.p`
  color: white;
  font-weight: 700;
  font-size: 48px;
`
