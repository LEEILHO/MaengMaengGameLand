import { styled } from 'styled-components'

export const VideoModalContainer = styled.div`
  width: 500px;
  height: 330px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: #a498ff;
  padding: 24px 0;
  border-radius: 16px;
`

export const Title = styled.h5`
  font-size: 24px;
  font-weight: 700;
  color: white;
`

export const TopRow = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  text-align: center;
  position: relative;
  margin-bottom: 24px;
`

export const CloseIcon = styled.img`
  width: 28px;
  height: 28px;
  margin-left: auto;
  position: absolute;
  right: 14px;
`
