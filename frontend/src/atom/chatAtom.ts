import { ChatMessageType } from '@type/waitingRoom/waitingRoom.type'
import { atom } from 'recoil'

export const ChatListState = atom<ChatMessageType[]>({
  key: 'chatList',
  default: [],
})
