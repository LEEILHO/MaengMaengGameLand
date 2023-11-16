import useModal from '@hooks/useModal'
import * as S from '@styles/user/RankItem.styled'
import { UserRecordType } from '@type/user/user.type'
import RecordModal from './RecordModal'

type Props = {
  record: UserRecordType
}

const RankItem = ({ record }: Props) => {
  const { Modal, isOpen, openModal, closeModal } = useModal()
  const changeGameName = () => {
    if (record.gameCategory === 'ALL_WIN_ROCK_SCISSOR_PAPER') {
      return '전승 가위바위보'
    }
    if (record.gameCategory === 'GOLD_SILVER_BRONZE') {
      return '금은동'
    }
    if (record.gameCategory === 'JEWELRY_AUCTION') {
      return '무제한 보석 경매'
    }
  }

  return (
    <>
      <S.RankItemContainer onClick={openModal}>
        <S.RankText>{record.rank}</S.RankText>
        <S.GameGategoryText>{changeGameName()}</S.GameGategoryText>
      </S.RankItemContainer>

      <Modal isOpen={isOpen} closeModal={closeModal}>
        <RecordModal
          closeModal={closeModal}
          gameCode={record.gameCode}
          gameCategory={`${changeGameName()}`}
        />
      </Modal>
    </>
  )
}

export default RankItem
