import { css, styled } from 'styled-components'

interface SettingButtonProps {
  $isSeleted: boolean
}

export const CreateRoomModalContainer = styled.div`
  max-width: 373px;
  max-height: 262px;
  width: 60vw;
  height: 75vh;
  border-radius: 24px;
  background: #f8f8f8;
  box-shadow: 4px 4px 4px 0px rgba(0, 0, 0, 0.25);
  display: flex;
  flex-direction: column;
`

export const TopRow = styled.div`
  width: 100%;
  display: flex;
  margin-top: 14px;
  justify-content: center;
  text-align: center;
  position: relative;
  margin-bottom: 16px;
`

export const Title = styled.h2`
  font-size: 20px;
  font-weight: 700;
  margin-top: 8px;
`

export const CloseIcon = styled.img`
  width: 28px;
  height: 28px;
  margin-left: auto;
  position: absolute;
  right: 14px;
`

export const SubTitle = styled.h6`
  font-weight: 700;
  font-size: 16px;
  margin-right: 14px;
  width: 66px;
  text-align: center;

  &.title {
    letter-spacing: 3.2px;
  }
`

export const SubRow = styled.div`
  width: 100%;
  height: auto;
  display: flex;
  justify-content: center;
  padding: 0 24px;
  align-items: center;
  margin-top: 24px;
`

export const RoomNameInput = styled.input`
  border-radius: 8px;
  background: #d9d9d9;
  flex: 1;
  height: 28px;
  font-size: 14px;
  font-weight: 500;
  padding: 0 12px;
`

export const ButtonRow = styled.div`
  width: 100%;
  display: flex;
  margin-top: auto;
  margin-bottom: 12px;
  align-items: center;
  justify-content: center;
  gap: 8px;
`

export const SettingButtonContainer = styled.div`
  display: flex;
  margin-right: auto;
  align-items: center;
  margin-top: auto;
  position: relative;
`

export const SettingButton = styled.div<SettingButtonProps>`
  color: white;
  height: 32px;
  border-radius: 4px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
  font-weight: 500;
  ${(props) =>
    props.$isSeleted
      ? css`
          z-index: 2;
          background-color: #7000ff;
          width: 52px;
        `
      : css`
          z-index: 1 back;
          border-radius: 0px 4px 4px 0px;
          background: #aed2ff;
          width: 56px;
        `}
`
