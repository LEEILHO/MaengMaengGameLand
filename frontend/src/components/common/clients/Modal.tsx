'use client'

import * as S from '@styles/common/Modal.styled'
import { ModalProps } from '@type/common/modal.type'
import { detectIosDevice } from '@utils/common/mobile'
import { AnimatePresence } from 'framer-motion'
import { useEffect, useState } from 'react'
import { createPortal } from 'react-dom'

const Modal = ({ children, isOpen, closeModal }: ModalProps) => {
  const [portalElement, setPortalElement] = useState<Element | null>(null)
  const [isIos, setIsIos] = useState(true)

  const closeHandler = (e: React.MouseEvent<HTMLElement>) => {
    e.stopPropagation()
    if (closeModal) closeModal()
  }

  useEffect(() => {
    setPortalElement(document.getElementById('portal'))
  }, [isOpen])

  useEffect(() => {
    setIsIos(detectIosDevice(window.navigator.userAgent))
  }, [])

  return (
    <>
      {portalElement
        ? createPortal(
            <AnimatePresence>
              {isOpen && (
                <S.Overlay onClick={closeHandler} className="modal-overlay">
                  <S.Wrapper
                    $isIos={isIos}
                    className="modal-wrapper"
                    onClick={(e) => e.stopPropagation()}
                    initial={{
                      opacity: 0,
                      scale: 0.75,
                      transform: 'translate(-50%, -50%)',
                    }}
                    animate={{
                      opacity: 1,
                      scale: 1,
                      transition: {
                        ease: 'easeOut',
                        duration: 0.25,
                      },
                    }}
                    exit={{
                      opacity: 0,
                      scale: 0.75,
                      transition: {
                        ease: 'easeIn',
                        duration: 0.2,
                      },
                    }}
                  >
                    {children}
                  </S.Wrapper>
                </S.Overlay>
              )}
            </AnimatePresence>,
            portalElement,
          )
        : null}
    </>
  )
}

export default Modal
