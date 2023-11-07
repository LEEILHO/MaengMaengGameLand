import { channelState } from '@atom/gameAtom'
import { userState } from '@atom/userAtom'
import CButton from '@components/common/clients/CButton'
import { images } from '@constants/images'
import * as S from '@styles/lobby/CreateRoomModal.styled'
import { gameTypeChange } from '@utils/lobby/lobbyUtil'
import { createRoom } from 'apis/lobby/lobbyApi'
import { usePathname, useRouter } from 'next/navigation'
import { useCallback, useEffect, useState } from 'react'
import { useRecoilValue, useRecoilValueLoadable } from 'recoil'

type Props = {
  closeModal: () => void
}

const CreateRoomModal = ({ closeModal }: Props) => {
  const router = useRouter()
  const pathname = usePathname()
  const [roomType, setRoomType] = useState<'공개' | '비공개'>('공개')
  const [title, setTitle] = useState('')
  const user = useRecoilValue(userState)
  const channel = useRecoilValue(channelState)

  const handleRoomType = useCallback((roomType: '공개' | '비공개') => {
    setRoomType(roomType)
  }, [])

  const handleTitileChange = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      setTitle(e.target.value)
    },
    [],
  )

  const handleCreateRoom = useCallback(() => {
    if (!user || !channel) return
    const isPublic = roomType === '공개'
    const gameType = gameTypeChange(pathname.split('/')[1])
    createRoom(title, isPublic, gameType, channel).then((res) => {
      closeModal()
      // todo: 방 입장
      console.log(res)
      router.replace(`waiting-room/${res.roomCode}`)
    })
  }, [title, roomType, channel, pathname])

  return (
    <S.CreateRoomModalContainer>
      <S.TopRow>
        <S.Title>방 만들기</S.Title>
        {/* <S.CloseIcon src={images.lobby.close} alt="닫기" onClick={closeModal} /> */}
      </S.TopRow>
      <S.SubRow>
        <S.SubTitle className="title">방제목</S.SubTitle>
        <S.RoomNameInput value={title} onChange={handleTitileChange} />
      </S.SubRow>
      <S.SubRow>
        <S.SubTitle>공개 설정</S.SubTitle>
        <S.SettingButtonContainer>
          <S.SettingButton
            onClick={() => handleRoomType('공개')}
            $isSeleted={roomType === '공개'}
          >
            공개
          </S.SettingButton>
          <S.SettingButton
            onClick={() => handleRoomType('비공개')}
            $isSeleted={roomType === '비공개'}
          >
            비공개
          </S.SettingButton>
        </S.SettingButtonContainer>
      </S.SubRow>
      <S.ButtonRow>
        <CButton
          text="방 생성"
          backgroundColor="#00A3FF"
          color="white"
          radius={32}
          fontSize={18}
          height={40}
          onClick={handleCreateRoom}
        />
        <CButton
          text="취소"
          backgroundColor="#D23838;"
          color="white"
          radius={32}
          fontSize={18}
          height={40}
          onClick={closeModal}
        />
      </S.ButtonRow>
    </S.CreateRoomModalContainer>
  )
}

export default CreateRoomModal
