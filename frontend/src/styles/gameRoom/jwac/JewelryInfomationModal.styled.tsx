import { images } from '@constants/images'
import { styled } from 'styled-components'

export const JewerlyInfomationContainer = styled.div`
  background-image: url(${images.gameRoom.jwac.jewelryInfomationFrame});
  background-position: center;
  background-size: cover;
  width: 40vw;
  height: 70vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`

export const Title = styled.h2`
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 32px;
`

export const Row = styled.div`
  display: flex;
  width: 100%;
  margin-bottom: 12px;
  justify-content: center;
  align-items: center;
`
export const JewerlyName = styled.p`
  font-weight: 500;
  font-size: 12px;
`
export const JewerlyCount = styled.p`
  font-weight: 500;
  font-size: 12px;
`
