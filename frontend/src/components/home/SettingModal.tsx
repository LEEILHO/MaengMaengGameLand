import { soundState } from '@atom/soundAtom'
import CButton from '@components/common/clients/CButton'
import { images } from '@constants/images'
import * as S from '@styles/home/SettingModal.styled'
import { useEffect, useState } from 'react'
import { useRecoilState } from 'recoil'

type Props = {
  closeModal: () => void
}

const spring = {
  type: 'spring',
  stiffness: 700,
  damping: 30,
}

const SettingModal = ({ closeModal }: Props) => {
  const [isSound, setIsSound] = useRecoilState(soundState)

  const handleSound = () => {
    // if (isSound) {
    //   localStorage.setItem('bgmSound', 'false')
    // } else {
    //   localStorage.setItem('bgmSound', 'true')
    // }
    setIsSound((prev) => !prev)
  }

  // useEffect(() => {
  //   if (localStorage.getItem('bgmSound') === 'false') {
  //     setIsSound(false)
  //   } else {
  //     setIsSound(true)
  //   }
  // }, [])

  return (
    <S.SettingRoomModalContainer>
      <S.TopRow>
        <S.Title>게임설정</S.Title>
        <S.CloseIcon src={images.lobby.close} alt="닫기" onClick={closeModal} />
      </S.TopRow>
      <S.SubRow>
        <S.SubTitle>배경음악</S.SubTitle>
        <S.SoundToggleContainer $isOn={isSound} onClick={handleSound}>
          <S.Handle layout transition={spring} />
        </S.SoundToggleContainer>
      </S.SubRow>
      <S.ButtonContainer>
        <CButton
          text="로그아웃"
          backgroundColor="#f72e2e"
          color="white"
          radius={32}
          fontSize={18}
          height={40}
        />
      </S.ButtonContainer>
    </S.SettingRoomModalContainer>
  )
}

export default SettingModal
