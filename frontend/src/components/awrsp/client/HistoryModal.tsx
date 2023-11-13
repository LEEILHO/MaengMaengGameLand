'use client'

import * as S from '@styles/awrsp/HistoryModal.styled'
import HistoryList from './HistoryList'

type Props = {
  closeModal: () => void
}

const HistoryModal = ({ closeModal }: Props) => {
  return (
    <S.HistoryModalContainer>
      <S.Title>카드 내역</S.Title>
      <S.Content>
        <HistoryList />
      </S.Content>
    </S.HistoryModalContainer>
  )
}

export default HistoryModal
