import { styled } from 'styled-components'

interface HeaderProps {}

export const HeaderContainer = styled.div`
  display: flex;
  position: fixed;
  width: 100%;
  top: 0;
  z-index: 3;
`

export const ProfileCard = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  position: absolute;
  left: 44px;
  top: 14px;
  height: 42px;
  border-radius: 16px;
  padding-left: 44px;
  padding-right: 24px;
  background: rgba(0, 0, 0, 0.2);
`

export const TierFrame = styled.div`
  position: absolute;
  top: -14px;
  left: -24px;
  .frame {
    position: relative;
    width: 65px;
    height: 75px;
    z-index: 1;
  }
`

export const ProfileImage = styled.img`
  position: absolute;
  top: 14px;
  left: 15px;
  width: 35px;
  height: 35px;
  border-radius: 50%;
`

export const UserName = styled.p`
  font-weight: 500;
  font-size: 14px;
  color: white;
`

export const SettingButton = styled.button`
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 44px;
  margin-top: 15px;
  background: none;
  cursor: pointer;
  transition: 0.25s;
  filter: drop-shadow(5px 5px 5px rgba(0, 0, 0, 0.25));

  &:active {
    filter: brightness(0.5) drop-shadow(5px 5px 2px rgba(0, 0, 0, 0.25));
  }

  img {
    height: 26px;
    width: 26px;
  }
`

export const FriendsButton = styled.button`
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  margin-top: 11px;
  margin-left: auto;
  background: none;
  cursor: pointer;
  transition: 0.25s;
  filter: drop-shadow(5px 5px 5px rgba(0, 0, 0, 0.25));

  &:active {
    filter: brightness(0.5) drop-shadow(5px 5px 2px rgba(0, 0, 0, 0.25));
  }

  img {
    height: 36px;
    width: 36px;
  }
`
