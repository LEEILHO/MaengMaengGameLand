'use client'

import { images } from '@constants/images'
import * as S from '@styles/user/RecordModal.styled'
import { useState, useEffect } from 'react'
import { RecordDetailType } from '@type/user/user.type'
import { getRecordHistoryDetail } from 'apis/user/userApi'
import RecordItem from './RecordItem'

type Props = {
  closeModal: () => void
  gameCode: string
  gameCategory: string
}

const RecordModal = ({ closeModal, gameCode, gameCategory }: Props) => {
  const [recordDetail, setRecordDetail] = useState<RecordDetailType[]>([])

  useEffect(() => {
    getRecordHistoryDetail(gameCode).then((res) => {
      setRecordDetail(res.data)
    })
  }, [])

  return (
    <S.ModalContainer>
      <S.TopRow>
        <S.Title>전적 상세 - {gameCategory}</S.Title>
        <S.CloseIcon
          src={images.lobby.close}
          alt={'닫기'}
          onClick={closeModal}
        />
      </S.TopRow>
      <S.PlayerList>
        {recordDetail.map((record) => (
          <RecordItem
            key={record.nickname}
            record={record}
            gameCategory={gameCategory}
          />
        ))}
      </S.PlayerList>
    </S.ModalContainer>
  )
}

export default RecordModal
