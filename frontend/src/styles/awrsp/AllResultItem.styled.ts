import { styled } from 'styled-components'

export const AllResultItemContainer = styled.div`
  width: 100%;
  height: 100%;

  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  padding: 8px;

  border-radius: 8px;
  background: rgba(217, 217, 217, 0.5);
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);

  position: relative;
`
export const ProfileImage = styled.img`
  width: 56px;
  height: 56px;
  border-radius: 50px;
`

export const Nickname = styled.p`
  color: #f5e340;
  text-align: center;
  font-size: 10px;
  font-weight: 600;
  line-height: normal;

  width: 60px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
`

export const WinCount = styled.p`
  color: #fff;
  text-align: center;
  text-shadow: 0px 2px 4px rgba(0, 0, 0, 0.25);
  font-size: 16px;
  font-weight: 700;
  line-height: normal;
`

export const Winner = styled.div`
  width: 100%;
  height: 100%;

  position: absolute;
  top: 0;
  left: 0;
  border-radius: 8px;
  background: rgba(0, 0, 0, 0.6);

  display: flex;
  justify-content: center;
  align-items: center;

  p {
    color: #fff;
    text-align: center;
    text-shadow: 0px 2px 4px rgba(0, 0, 0, 0.25);
    font-size: 22px;
    font-weight: 700;
    line-height: normal;
  }
`
