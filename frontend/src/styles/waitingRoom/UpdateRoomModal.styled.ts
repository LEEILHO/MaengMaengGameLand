import { colors } from '@constants/colors'
import { css, styled } from 'styled-components'

interface ToggleType {
  isPublic: boolean
}

export const UpdateRoomModalContainer = styled.div`
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

export const PublicButtonContainer = styled.div`
  display: flex;
  margin-right: auto;
  align-items: center;
  margin-top: auto;
  position: relative;
`
export const CheckBox = styled.input`
  display: none;
`
export const CheckBoxLable = styled.label<ToggleType>`
  z-index: 5;
  width: 130px;
  height: 32px;
  border-radius: 4px;
  background: ${colors.primary[100]};
  color: white;
  /* state === false일 때의 before */
  &::before {
    display: flex;
    position: absolute;
    content: '공개';
    padding-left: 20px;
    justify-content: flex-start;
    align-items: center;
    width: 100px;
    height: 32px;
    transition: all 0.2s ease-in-out;
  }

  /* state === false일 때 after */
  &::after {
    display: flex;
    position: relative;
    content: '비공개';
    width: 65px;
    height: 32px;
    justify-content: center;
    align-items: center;
    left: 65px;
    border-radius: 4px;
    background-color: ${colors.primary[300]};
    box-shadow: 1px 2px 8px rgba(0, 0, 0, 0.16);
    transition: all 0.2s ease-in-out;
  }

  /* state가 true일 때 */
  ${(props) =>
    props.isPublic &&
    `
    &::before {
        padding-right: 1rem;
        content: '비공개';
        justify-content: flex-end;
    };

    &::after {
        content: '공개';
        width: 65px;
        height: 32px;
        left: 0;
    }
  `}
`
