import * as S from '@styles/lobby/RoomList.styled'
import RoomItem from './RoomItem'
import RoomTypeButton from './RoomTypeButton'
import { useCallback, useState } from 'react'
import { useRouter } from 'next/navigation'

const RoomList = () => {
  const [seletedRoomType, setSeletedRoomType] = useState<
    '자유' | '브론즈' | '실버' | '골드'
  >('자유')

  const handleTypeButton = useCallback(
    (type: '자유' | '브론즈' | '실버' | '골드') => {
      setSeletedRoomType(type)
    },
    [],
  )

  const router = useRouter()

  return (
    <S.RoomListContainer>
      <S.TypeButtonRaw>
        <RoomTypeButton
          isActive={seletedRoomType === '자유' ? 'ACTIVE' : 'INACTIVE'}
          type="자유"
          handleTypeButton={handleTypeButton}
        />
        <RoomTypeButton
          isActive={seletedRoomType === '브론즈' ? 'ACTIVE' : 'INACTIVE'}
          type="브론즈"
          handleTypeButton={handleTypeButton}
        />
        <RoomTypeButton
          isActive={seletedRoomType === '실버' ? 'ACTIVE' : 'INACTIVE'}
          type="실버"
          handleTypeButton={handleTypeButton}
        />
        <RoomTypeButton
          isActive={seletedRoomType === '골드' ? 'ACTIVE' : 'INACTIVE'}
          type="골드"
          handleTypeButton={handleTypeButton}
        />
      </S.TypeButtonRaw>
      <S.RoomBox>
        <RoomItem
          title="1번방 1번방 1번방 1번방"
          maxPeople="8"
          curPeople="3"
          onClick={() => {
            router.push('gsb/waiting-room/1')
          }}
        />
        <RoomItem title="1번방 2번방 2번방 2번방" maxPeople="8" curPeople="2" />
        <RoomItem title="3번방 3번방 3번방 3번방" maxPeople="8" curPeople="1" />
        <RoomItem title="4번방 4번방 4번방 4번방" maxPeople="8" curPeople="8" />
        <RoomItem title="1번방 1번방 1번방 1번방" maxPeople="8" curPeople="3" />
        <RoomItem title="1번방 2번방 2번방 2번방" maxPeople="8" curPeople="2" />
        <RoomItem title="3번방 3번방 3번방 3번방" maxPeople="8" curPeople="1" />
        <RoomItem title="4번방 4번방 4번방 4번방" maxPeople="8" curPeople="8" />
      </S.RoomBox>
    </S.RoomListContainer>
  )
}

export default RoomList
