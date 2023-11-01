import { colors } from '@constants/colors'
import { styled } from 'styled-components'

export const ChattingContainer = styled.div`
  width: 239px;
  height: 266px;
  border-radius: 16px;
  background: rgba(228, 241, 255, 0.5);
  display: flex;
  flex-direction: column;

  @media screen and ((max-width: 700px) or (max-height: 376px)) {
    width: 200px;
    height: 234px;
  }
`

export const MessageArea = styled.div`
  height: 100%;
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  margin: 16px;
  overflow: auto;
`

export const ChattingInput = styled.input`
  border-bottom: white solid 2px;
  width: calc(100% - 32px);
  margin: 0px 16px 16px 16px;
  padding-bottom: 6px;
  background: none;
  font-family: 'kbo-dia';

  &::placeholder {
    color: rgba(255, 255, 255, 0.5);
    font-size: 12px;
    font-weight: 500;
    font-family: 'kbo-dia';
  }
`

export const ChatBubble = styled.p`
  color: #fff;
  font-size: 12px;
  font-weight: 500;
  line-height: normal;
  margin-bottom: 6px;
`
