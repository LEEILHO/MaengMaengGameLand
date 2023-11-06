import * as S from '@styles/lobby/RoomList.styled'
import RoomItem from './RoomItem'
import RoomTypeButton from './RoomTypeButton'
import { useCallback, useEffect, useState } from 'react'
import { usePathname, useRouter } from 'next/navigation'
import { useRecoilState, useRecoilValue, useSetRecoilState } from 'recoil'
import { channelState, gameTypeState } from '@atom/gameAtom'
import { channelTypeChange, gameTypeChange } from '@utils/lobby/lobbyUtil'
import useSocket from '@hooks/useSocketLobby'
import { RoomType } from '@type/lobby/lobby.type'
import { roomsState } from '@atom/lobbyAtom'
import { loadRooms } from 'apis/lobby/lobbyApi'

const RoomList = () => {
  const router = useRouter()
  const pathname = usePathname()
  const [seletedRoomType, setSeletedRoomType] = useState<
    '자유' | '브론즈' | '실버' | '골드'
  >('자유')
  const [rooms, setRooms] = useRecoilState(roomsState)
  const [channel, setChannel] = useRecoilState(channelState)
  const gameType = gameTypeChange(pathname.split('/')[1])

  const handleTypeButton = useCallback(
    (type: '자유' | '브론즈' | '실버' | '골드') => {
      setSeletedRoomType(type)
      setChannel(channelTypeChange(type))
    },
    [],
  )

  // 방 목록 불러오기
  useEffect(() => {
    console.log(`방 새로 불러오기 -> ${gameType}`)
    if (!gameType || !channel) return
    loadRooms(gameType, channel).then((res) => {
      setRooms(res)
      console.log(res)
    })
  }, [gameType, channel])

  // 초기 채널 설정
  useEffect(() => {
    setChannel(channelTypeChange('자유'))
  }, [])

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
      <div
        style={{
          display: 'flex',
          width: '100%',
          height: '100%',
          justifyContent: 'center',
          alignItems: 'center',
        }}
      >
        <S.RoomBox>
          {rooms?.map((room) => (
            <RoomItem
              key={room.roomCode}
              title={room.title}
              maxPeople={room.maxHeadCount}
              curPeople={room.headCount}
              roomCode={room.roomCode}
            />
          ))}
        </S.RoomBox>
      </div>
    </S.RoomListContainer>
  )
}

export default RoomList
