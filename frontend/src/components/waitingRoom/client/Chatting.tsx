import React, { useEffect, useState } from 'react'
import * as S from '@styles/waitingRoom/Chatting.styled'

const Chatting = () => {
  const [newMessage, setNewMessage] = useState('')

  const onChangeMessage = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNewMessage(e.target.value)
  }

  const onKeyDownEnter = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      if (newMessage) {
        console.log(newMessage)
        setNewMessage('')
      }
    }
  }

  return (
    <S.ChattingContainer>
      <S.MessageArea>
        <S.ChatBubble>
          맹박사 : 맹박사님을 아세요???맹박사님을 아세요???맹박사님을
          아세요???맹박사님을 아세요???맹박사님을 아세요???맹박사님을
          아세요???맹박사님을 아세요???
        </S.ChatBubble>
      </S.MessageArea>
      <S.ChattingInput
        type="text"
        placeholder="하고 싶은 말을 입력해주세요."
        value={newMessage}
        onChange={(e) => onChangeMessage(e)}
        onKeyDown={(e) => onKeyDownEnter(e)}
      />
    </S.ChattingContainer>
  )
}

export default Chatting
