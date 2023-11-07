import CButton from '@components/common/clients/CButton'
import { useCallback } from 'react'
import * as S from '@styles/lobby/RoomItem.styled'
import { usePathname, useRouter } from 'next/navigation'

type Props = {
  title: string
  curPeople: number
  maxPeople: number
  roomCode: string
}

const RoomItem = ({ title, curPeople, maxPeople, roomCode }: Props) => {
  const router = useRouter()

  const hnadleEnter = useCallback(() => {
    router.replace(`waiting-room/${roomCode}`)
  }, [])

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
          onClick={hnadleEnter}
        />
      </S.BottomRow>
    </S.RoomItemContainer>
  )
}

export default RoomItem
