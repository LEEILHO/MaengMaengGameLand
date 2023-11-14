'use client'

import * as S from '@styles/gsb/AlertModal.styled'
import { useEffect } from 'react'

type Props = {
  text: string
  closeModal: () => void
}

const AlertModal = ({ text, closeModal }: Props) => {
  useEffect(() => {
    setTimeout(closeModal, 2000)
  }, [])

  return <S.Container>{text}</S.Container>
}

export default AlertModal
