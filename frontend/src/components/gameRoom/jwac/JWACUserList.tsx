import * as S from '@styles/gameRoom/jwac/JWACUserList.styled'
import JWACUserItem from './JWACUserItem'
import { Empty } from '@styles/gameRoom/jwac/JWACUserItem.styled'

const JWACUserList = () => {
  return (
    <S.JWACUserListContainer>
      <JWACUserItem />
      <JWACUserItem />
      <JWACUserItem />
      <JWACUserItem />
      {/* <JWACUserItem />
      <JWACUserItem /> */}
      <Empty />
      <Empty />
      <Empty />
      <Empty />
      {/* <JWACUserItem />
      <JWACUserItem /> */}
    </S.JWACUserListContainer>
  )
}

export default JWACUserList
