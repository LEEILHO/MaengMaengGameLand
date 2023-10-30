import { images } from '@constants/images'
import * as S from '@styles/common/BackButton.styled'
import { useRouter } from 'next/navigation'

type Props = {
  size: number
}

const BackButton = ({ size }: Props) => {
  const router = useRouter()

  return (
    <S.BackButton $size={size} onClick={() => router.back()}>
      <S.BackButtonImage
        $size={size}
        src={images.common.header.back}
        alt="뒤로가기"
      />
    </S.BackButton>
  )
}

export default BackButton
