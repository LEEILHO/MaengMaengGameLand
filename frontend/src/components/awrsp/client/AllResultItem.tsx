'use client'

import { images } from '@constants/images'
import * as S from '@styles/awrsp/AllResultItem.styled'

const AllResultItem = () => {
  return (
    <S.AllResultItemContainer>
      <S.ProfileImage src={images.common.header.dummyProfile} />
      <S.Nickname>김상근</S.Nickname>
      <S.WinCount>3승</S.WinCount>

      {/* <S.Winner>
        <p>1등</p>
      </S.Winner> */}
    </S.AllResultItemContainer>
  )
}

export default AllResultItem
