import { UserRecordResponseType, WatchCodeType } from '@type/user/user.type'
import { authAxios, authHttp } from '@utils/http'

export async function editUser(
  nickname: string,
): Promise<{ nickname: string }> {
  return authHttp.post(`user/edit/nickname`, {
    nickname: nickname,
  })
}

export async function editProfileImage(
  profileImage: FormData,
): Promise<{ profileInage: FormData }> {
  return authHttp.post(`user/edit/profile`, profileImage, {
    'Content-Type': 'multipart/form-data',
  })
}

export async function getRecordHistory(): Promise<UserRecordResponseType> {
  return authAxios.get(`record/history`)
}

export async function getWatchLoginCode(): Promise<WatchCodeType> {
  return authAxios.get(`user/watch`)
}
