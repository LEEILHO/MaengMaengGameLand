import CButton from '@components/common/clients/CButton'
import * as S from '@styles/lobby/RoomItem.styled'

type Props = {
  title: string
  curPeople: string
  maxPeople: string
}

const RoomItem = ({ title, curPeople, maxPeople }: Props) => {
  return (
    <S.RoomItemContainer>
      <S.Title>{title}</S.Title>
      <S.BottomRow>
        <S.NumberOfPeople>{`${curPeople} / ${maxPeople}`}</S.NumberOfPeople>
        <CButton
          text="입장"
          backgroundColor="rgba(88, 21, 172, 1)"
          radius={16}
          width={64}
          height={32}
          fontSize={14}
          color="white"
        />
      </S.BottomRow>
    </S.RoomItemContainer>
  )
}

export default RoomItem
