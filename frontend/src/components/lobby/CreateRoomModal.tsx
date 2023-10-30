import CButton from '@components/common/clients/CButton'
import { images } from '@constants/images'
import * as S from '@styles/lobby/CreateRoomModal'

type Props = {
  closeModal: () => void
}

const CreateRoomModal = ({ closeModal }: Props) => {
  return (
    <S.CreateRoomModalContainer>
      <S.TopRow>
        <S.Title>방 만들기</S.Title>
        {/* <S.CloseIcon src={images.lobby.close} alt="닫기" onClick={closeModal} /> */}
      </S.TopRow>
      <S.SubRow>
        <S.SubTitle className="title">방제목</S.SubTitle>
        <S.RoomNameInput />
      </S.SubRow>
      <S.SubRow>
        <S.SubTitle>공개 설정</S.SubTitle>
        <S.SettingButtonContainer>
          <S.SettingButton $isSeleted={true}>공개</S.SettingButton>
          <S.SettingButton $isSeleted={false}>비공개</S.SettingButton>
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
