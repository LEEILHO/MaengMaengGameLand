import {
  ChannelTireType,
  CreateRoomResponseType,
  GameCategoryType,
  RoomType,
} from '@type/lobby/lobby.type'
import { authHttp } from '@utils/http'

export async function createRoom(
  title: string,
  publicRoom: boolean,
  gameCategory: GameCategoryType,
  channelTire: ChannelTireType,
): Promise<CreateRoomResponseType> {
  return authHttp.post(`room/create`, {
    title: title,
    publicRoom: publicRoom,
    gameCategory: gameCategory,
    channelTire: channelTire,
  })
}

export async function loadRooms(
  gameCategory: GameCategoryType,
  channelTire: ChannelTireType,
): Promise<RoomType[]> {
  return authHttp.get(`lobby/${gameCategory}/${channelTire}`)
}
