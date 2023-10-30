import { styled } from 'styled-components'
import { colors } from '@constants/colors'

export const PlayerCardContainer = styled.div`
  width: 100%;
  height: 100%;
  position: relative;
  border-radius: 16px;
  background: ${colors.primary[100]};
`

export const UserInfo = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: center;
`

export const UserNickname = styled.p`
  text-align: center;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 4px;
`

export const TierFrame = styled.div`
  position: relative;
  .frame {
    position: relative;
    width: 93px;
    height: 106px;
    z-index: 1;
  }
`

export const ProfileImage = styled.img`
  position: absolute;
  top: 20px;
  left: 22px;
  width: 50px;
  height: 50px;
`

export const UserDetailButton = styled.button`
  position: absolute;
  top: 8px;
  right: 3px;
  background: none;

  z-index: 2;

  img {
    width: 16px;
    height: 16px;
  }
`

export const ManagerMark = styled.img`
  position: absolute;
  top: 8px;
  left: 8px;
  width: 20px;
  height: 20px;
`

export const ReadyMark = styled.img`
  position: absolute;
  top: 8px;
  left: 8px;
  width: 20px;
  height: 20px;
`
export const EmptyPlayerCardContainer = styled.div`
  width: 119px;
  height: 127px;
  border-radius: 16px;
  background: rgba(228, 241, 255, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
`

export const InActiveMark = styled.img`
  width: 62px;
  height: 62px;
`
