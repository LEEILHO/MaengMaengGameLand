import { soundState } from '@atom/soundAtom'
import CButton from '@components/common/clients/CButton'
import { images } from '@constants/images'
import useSound from '@hooks/useSound'
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
  const { playButtonSound } = useSound()

  const handleSound = (type: 'BGM' | 'EFFECT') => {
    // setIsSound((prev) => !prev)
    if (type === 'BGM') {
      setIsSound((prev) => {
        return {
          bgmSound: prev?.bgmSound ? !prev.bgmSound : true,
          effectSound: prev?.effectSound === null ? true : prev!.effectSound,
        }
      })
      return
    }
    if (type === 'EFFECT') {
      setIsSound((prev) => {
        return {
          bgmSound: prev?.bgmSound === null ? true : prev!.bgmSound,
          effectSound: prev?.effectSound ? !prev.effectSound : true,
        }
      })
      return
    }
  }

  const handleClose = () => {
    playButtonSound()
    closeModal()
  }

  return (
    <S.SettingRoomModalContainer>
      <S.TopRow>
        <S.Title>게임설정</S.Title>
        <S.CloseIcon
          src={images.lobby.close}
          alt="닫기"
          onClick={handleClose}
        />
      </S.TopRow>
      <S.SubRow>
        <S.SubTitle>배경음악</S.SubTitle>
        <S.SoundToggleContainer
          $isOn={isSound?.bgmSound ?? true}
          onClick={() => handleSound('BGM')}
        >
          <S.Handle layout transition={spring} />
        </S.SoundToggleContainer>
      </S.SubRow>
      <S.SubRow>
        <S.SubTitle>효과음</S.SubTitle>
        <S.SoundToggleContainer
          $isOn={isSound?.effectSound ?? true}
          onClick={() => handleSound('EFFECT')}
        >
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
