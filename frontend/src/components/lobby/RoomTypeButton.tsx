import * as S from '@styles/lobby/RoomTypeButton.styled'

type Props = {
  isActive: 'ACTIVE' | 'INACTIVE'
  type: '자유' | '브론즈' | '실버' | '골드'
  handleTypeButton: (type: '자유' | '브론즈' | '실버' | '골드') => void
}

const RoomTypeButton = ({ isActive, type, handleTypeButton }: Props) => {
  return (
    <S.RoomTypeButtonContainer>
      <S.RoomTypeButton $type={isActive} onClick={() => handleTypeButton(type)}>
        {type}
      </S.RoomTypeButton>
    </S.RoomTypeButtonContainer>
  )
}

export default RoomTypeButton
