import styled, { css } from 'styled-components'
import { motion } from 'framer-motion'

type SwitchProps = {
  $isOn: boolean
}

export const SettingRoomModalContainer = styled.div`
  width: 300px;
  height: 300px;
  border-radius: 24px;
  background-color: #f8f8f8;
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
  margin-bottom: 32px;
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
  text-align: left;
  margin-left: 24px;

  &.title {
    letter-spacing: 3.2px;
  }
`

export const SubRow = styled.div`
  width: 100%;
  height: auto;
  display: flex;
  margin-top: 24px;
  align-items: center;
  justify-content: space-between;
`

export const SoundToggleContainer = styled.div<SwitchProps>`
  width: 70px;
  height: 40px;
  background-color: rgba(136, 136, 136, 0.4);
  display: flex;
  justify-content: flex-start;
  align-items: center;
  border-radius: 50px;
  padding: 6px;
  cursor: pointer;
  margin-right: 12px;

  ${(props) =>
    props.$isOn &&
    css`
      justify-content: flex-end;
      background-color: rgba(163, 138, 255, 0.4);
    `}
`

export const Handle = styled(motion.div)`
  width: 30px;
  height: 30px;
  background-color: white;
  border-radius: 40px;
`

export const ButtonContainer = styled.div`
  display: flex;
  width: 100%;
  align-items: center;
  margin-top: auto;
  position: relative;
  justify-content: right;
  margin-bottom: 12px;
  padding-right: 12px;
`
