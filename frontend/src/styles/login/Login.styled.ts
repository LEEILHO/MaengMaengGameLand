import { styled } from 'styled-components'
import Lottie from 'lottie-react'
import { images } from '@constants/constants'

export const Login = styled.div`
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0;
  background-image: url(${images.login.background});
  background-repeat: no-repeat;
  background-size: cover;
  overflow: hidden;
`

export const Rocket = styled(Lottie)`
  width: 600px;
  height: 700px;
  position: fixed;
  top: 50%;
  left: 85%;
  transform: translate(-50%, -50%);
  z-index: 3;
`

export const Title = styled.img`
  position: fixed;
  left: 50%;
  top: 35%;
  transform: translate(-50%, -50%);
  z-index: 5;
`

export const Announcement = styled.p`
  color: #fff;
  text-align: center;
  font-size: 16px;
  font-weight: 700;
  letter-spacing: -0.32px;

  position: fixed;
  left: 50%;
  bottom: 35%;
  transform: translate(-50%, -50%);
  z-index: 5;

  // 글씨 반짝이는 애니메이션
  @keyframes blink-effect {
    0% {
      opacity: 1;
    }

    50% {
      opacity: 0.2;
    }
  }
  animation: blink-effect 3s ease-in-out infinite;
`
