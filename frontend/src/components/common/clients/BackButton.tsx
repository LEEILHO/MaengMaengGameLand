import { images } from '@constants/images'
import * as S from '@styles/common/BackButton.styled'

type Props = {
  size: number
  handleBack: () => void
}

const BackButton = ({ size, handleBack }: Props) => {
  return (
    <S.BackButton
      $size={size}
      onClick={() => {
        handleBack()
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
