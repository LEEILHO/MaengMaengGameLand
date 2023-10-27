'use client'

import * as S from '@styles/common/Modal.styled'
import { ModalProps } from '@type/common/modal.type'
import { useEffect, useState } from 'react'
import { createPortal } from 'react-dom'

const Modal = ({ children, isOpen, closeModal }: ModalProps) => {
  const [portalElement, setPortalElement] = useState<Element | null>(null)

  const closeHandler = (e: React.MouseEvent<HTMLElement>) => {
    e.stopPropagation()
    if (closeModal) closeModal()
  }

  useEffect(() => {
    setPortalElement(document.getElementById('portal'))
  }, [isOpen])

  return (
    <>
      {isOpen && portalElement
        ? createPortal(
            <S.Overlay onClick={closeHandler} className="modal-overlay">
              <S.Wrapper onClick={(e) => e.stopPropagation()}>
                {children}
              </S.Wrapper>
            </S.Overlay>,
            portalElement,
          )
        : null}
    </>
  )
}

export default Modal
