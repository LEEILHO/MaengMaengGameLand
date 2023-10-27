import { images } from '@constants/images'
import * as S from '@styles/common/BackButton.styled'
import { useRouter } from 'next/navigation'

const BackButton = () => {
  const router = useRouter()

  return (
    <S.BackButton onClick={() => router.back()}>
      <S.BackButtonImage src={images.common.header.back} alt="뒤로가기" />
    </S.BackButton>
  )
}

export default BackButton
