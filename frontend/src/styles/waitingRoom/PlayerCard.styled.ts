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

  @media screen and ((max-width: 700px) or (max-height: 376px)) {
    .frame {
      width: 75px;
      height: 90px;
    }
  }
`

export const ProfileImage = styled.img`
  position: absolute;
  border-radius: 50%;
  top: 20px;
  left: 22px;
  width: 50px;
  height: 50px;

  @media screen and ((max-width: 700px) or (max-height: 376px)) {
    top: 17px;
    left: 17px;
    width: 42px;
    height: 42px;
  }
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

  @media screen and ((max-width: 700px) or (max-height: 376px)) {
    top: 4px;
    right: 1px;
  }
`

export const LeftTopMark = styled.img`
  position: absolute;
  top: 8px;
  left: 8px;
  width: 20px;
  height: 20px;

  @media screen and ((max-width: 700px) or (max-height: 376px)) {
    top: 6px;
    left: 6px;
    width: 16px;
    height: 16px;
  }
`
export const EmptyPlayerCardContainer = styled.div`
  width: 100%;
  height: 100%;
  border-radius: 16px;
  background: rgba(228, 241, 255, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
`

export const InActiveMark = styled.img`
  width: 62px;
  height: 62px;

  @media screen and ((max-width: 700px) or (max-height: 376px)) {
    width: 48px;
    height: 48px;
  }
`
