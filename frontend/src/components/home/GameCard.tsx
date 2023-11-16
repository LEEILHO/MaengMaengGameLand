import VideoModal from '@components/common/clients/VideoModal'
import { images } from '@constants/images'
import useModal from '@hooks/useModal'
import * as S from '@styles/home/GameCard.styled'

type Props = {
  backGroundUrl: string
  name: string
  onClick: () => void
}

const GameCard = ({ backGroundUrl, name, onClick }: Props) => {
  const { Modal, closeModal, openModal, isOpen } = useModal()

  const handleDiscriptionIcon = (
    event: React.MouseEvent<HTMLImageElement, MouseEvent>,
  ) => {
    event.stopPropagation()
    openModal()
  }

  return (
    <>
      <Modal isOpen={isOpen} closeModal={closeModal}>
        <VideoModal title={name} closeModal={closeModal} />
      </Modal>
      <S.GameCardContainer>
        <S.GameImageContainer $backGroundUrl={backGroundUrl} onClick={onClick}>
          <S.DiscriptionIcon
            src={images.home.discription}
            alt="설명"
            onClick={handleDiscriptionIcon}
          />
        </S.GameImageContainer>
        <S.GameName>{name}</S.GameName>
      </S.GameCardContainer>
    </>
  )
}

export default GameCard
