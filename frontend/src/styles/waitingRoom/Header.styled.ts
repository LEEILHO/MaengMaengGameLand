import { styled } from 'styled-components'

export const HeaderContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  position: absolute;
  top: 0;
  left: 0;
  z-index: 3;
`

export const RoomInfo = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 15px;
  margin-left: 44px;
  gap: 12px;

  p {
    color: #fff;
    font-size: 16px;
    font-weight: 500;
  }

  img {
    width: 24;
    height: 24;
  }
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
