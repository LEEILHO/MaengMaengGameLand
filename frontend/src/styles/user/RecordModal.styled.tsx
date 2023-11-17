import { styled } from 'styled-components'

export const ModalContainer = styled.div`
  width: 400px;
  height: 200px;
  background-color: #f8f8f8;
  border-radius: 24px;
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

export const PlayerList = styled.div`
  width: 100%;
  padding: 0px 24px;
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  gap: 6px;
  overflow-x: auto;
`
