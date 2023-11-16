import { colors } from '@constants/colors'
import { images } from '@constants/images'
import { styled } from 'styled-components'

export const UserProfileSideBarContainer = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  width: 144px;
  align-items: center;
`

export const UserProfileBox = styled.div`
  width: 66px;
  height: 66px;
  margin-top: 24px;
  position: relative;
  cursor: pointer;

  &::before {
    content: '';
    display: inline-block;
    background-image: url(${images.user.camera});
    background-repeat: no-repeat;
    background-size: 22px 22px;
    width: 22px;
    height: 22px;
    position: absolute;
    right: 0;
    bottom: 0;
  }
`

export const UserProfileImage = styled.img`
  width: 66px;
  height: 66px;
  border-radius: 50%;
  object-fit: cover;
`

export const UserNameRow = styled.div`
  display: flex;
  margin-top: 22px;
  align-items: center;
`

export const UserName = styled.p`
  color: white;
  font-weight: 500;
  font-size: 14px;
  margin-right: 8px;
`

export const UserNameInput = styled.input`
  color: white;
  font-size: 12px;
  font-weight: 500;
  margin-right: 8px;
  width: 100%;
  height: 24px;
  margin-left: 8px;
  padding: 0 8px;
  background-color: rgba(255, 255, 255, 0.3);
  padding-right: 24px;
`

export const UserNameInputContainer = styled.div`
  display: flex;
  position: relative;
  align-items: center;
`

export const UserNameEditIcon = styled.img`
  width: 14px;
  height: 14px;
  cursor: pointer;
`

export const UserNameSubmitIcon = styled.img`
  width: 16px;
  height: 16px;
  cursor: pointer;
  margin-right: 8px;
  position: absolute;
  right: 4px;
`

export const GoBackIcon = styled.img`
  width: 22px;
  height: 22px;
  margin: auto auto 16px 16px;
  cursor: pointer;
`

export const CodeRow = styled.div`
  display: flex;
  margin-top: 22px;
  align-items: center;
  border-bottom: 1px solid white;
  padding-bottom: 4px;
`

export const GenerateCodeButton = styled.p`
  color: white;
  font-weight: 500;
  font-size: 14px;

  &:active {
    color: rgba(256, 256, 256, 0.5);
  }
`
