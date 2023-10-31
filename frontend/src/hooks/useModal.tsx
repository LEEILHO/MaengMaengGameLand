'use client'

import Modal from '@components/common/clients/Modal'
import { ModalProps } from '@type/common/modal.type'
import React, { ReactNode, useCallback, useEffect, useState } from 'react'
import useBodyScrollLock from './useBodyScrollLock'

type UseModalReturnType = {
  Modal: ({ children, isOpen, closeModal }: ModalProps) => JSX.Element
  isOpen: boolean
  openModal: () => void
  closeModal: () => void
}

const useModal = (): UseModalReturnType => {
  const { lockScroll, openScroll } = useBodyScrollLock()
  // 모달의 렌더링 여부를 설정할 상태 값
  const [isOpen, setIsOpen] = useState(false)

  // 모달 열기
  const openModal = useCallback(() => {
    setIsOpen(true)
    lockScroll()
  }, [])

  // 모달 닫기
  const closeModal = useCallback(() => {
    setIsOpen(false)
    openScroll()
  }, [])

  // isOpen이 true라면 Modal 컴포넌트 반환, false라면 null 반환
  return { Modal, isOpen, openModal, closeModal }
}

export default useModal
