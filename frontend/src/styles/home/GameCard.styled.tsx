import { styled } from 'styled-components'

interface GameCardProps {
  $backGroundUrl: string
}

export const GameCardContainer = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  flex-direction: column;
`

export const GameImageContainer = styled.div<GameCardProps>`
  width: 186px;
  height: 186px;
  border-radius: 24px;
  background: url(${(props) => props.$backGroundUrl});
  background-size: cover;
  background-position: center center;
  background-repeat: no-repeat;
`

export const DiscriptionIcon = styled.img`
  position: absolute;
  top: 14px;
  right: 14px;
  height: 16px;
  width: 16px;
`

export const GameName = styled.p`
  font-size: 20px;
  color: white;
  font-weight: 700;
  margin-top: 24px;
`
