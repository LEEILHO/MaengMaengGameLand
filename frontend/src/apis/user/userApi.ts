import { authHttp } from '@utils/http'

export async function editUser(
  nickname: string,
): Promise<{ nickname: string }> {
  return authHttp.post(`user/edit/nickname`, {
    nickname: nickname,
  })
}
