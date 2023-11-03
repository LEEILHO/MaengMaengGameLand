import {
  ChannelTireType,
  CreateRoomRequestType,
  GameCategoryType,
} from '@type/lobby/lobby.type'
import { authHttp } from '@utils/http'

export async function createRoom(
  title: string,
  publicRoom: boolean,
  host: string,
  gameCategory: GameCategoryType,
  channelTire: ChannelTireType,
): Promise<CreateRoomRequestType> {
  return authHttp.post(`room/create`, {
    title: title,
    publicRoom: publicRoom,
    host: host,
    gameCategory: gameCategory,
    channelTire: channelTire,
  })
}
