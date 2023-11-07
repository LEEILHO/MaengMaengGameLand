import React, { useEffect, useRef, useState } from 'react'
import * as S from '@styles/waitingRoom/Chatting.styled'
import { useRecoilValue } from 'recoil'
import { userState } from '@atom/userAtom'
import { usePathname } from 'next/navigation'
import { ChatListState } from '@atom/chatAtom'

type Props = {
  handleSendChat: (msg: string) => void
}

const Chatting = ({ handleSendChat }: Props) => {
  const [newMessage, setNewMessage] = useState('')
  const chatList = useRecoilValue(ChatListState)
  const chatEndRef = useRef<HTMLDivElement | null>(null)

  const onChangeMessage = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNewMessage(e.target.value)
  }

  const onKeyDownEnter = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      if (newMessage) {
        handleSendChat(newMessage)

        setNewMessage('')
      }
    }
  }

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: 'smooth' })
  }, [chatList])

  return (
    <S.ChattingContainer>
      <S.MessageArea>
        {chatList?.map((chat) => (
          <S.ChatBubble>
            {chat.nickname} : {chat.message}
          </S.ChatBubble>
        ))}
        <div ref={chatEndRef}></div>
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
