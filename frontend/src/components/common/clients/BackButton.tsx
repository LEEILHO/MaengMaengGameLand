import { images } from '@constants/images'
import * as S from '@styles/common/BackButton.styled'
import { useRouter } from 'next/navigation'

type Props = {
  size: number
  handleBack?: () => void
}

const BackButton = ({ size, handleBack }: Props) => {
  const router = useRouter()

  return (
    <S.BackButton
      $size={size}
      onClick={() => {
        if (handleBack) {
          handleBack()
        }
        router.back()
      }}
    >
      <S.BackButtonImage
        $size={size}
        src={images.common.header.back}
        alt="뒤로가기"
      />
    </S.BackButton>
  )
}

export default BackButton
