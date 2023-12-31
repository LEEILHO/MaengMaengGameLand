'use client'

import * as S from '@styles/waitingRoom/PlayerCard.styled'
import { images } from '@constants/images'
import useModal from '@hooks/useModal'
import UserDetailModal from './UserDetailModal'
import { Participant } from '@type/waitingRoom/seat.type'
import { useCallback } from 'react'
import { useRecoilValue } from 'recoil'
import { userState } from '@atom/userAtom'
import useSound from '@hooks/useSound'

type Props = {
  user?: Participant
  isOpened: boolean
  isHost: boolean
  handleEmptySeat: (index: number) => void
  handleKick: (kickPlayer: string) => void
  index: number
}

const PlayerCard = ({
  user,
  isOpened,
  isHost,
  handleEmptySeat,
  handleKick,
  index,
}: Props) => {
  const { Modal, isOpen, openModal, closeModal } = useModal()
  const myInfo = useRecoilValue(userState)
  const { playButtonSound } = useSound()

  const getTierImage = useCallback((tier: string) => {
    if (tier === 'BRONZE') return images.common.header.bronzeFrame
    else if (tier === 'SILVER') return images.common.header.silverFrame
    else if (tier === 'GOLD') return images.common.header.goldFrame
    else if (tier === 'CHALLENGER') return images.common.header.challengerFrame
  }, [])

  const onClickDetailButton = useCallback(() => {
    playButtonSound()
    openModal()
  }, [])

  const onClickEmptySeat = (index: number) => {
    playButtonSound()
    handleEmptySeat(index)
  }

  // 유저 칸이 닫혀 있는지 아닌지 확인
  return isOpened ? (
    // 열려 있을 때, 칸을 차지한 유저가 있다면
    user ? (
      <>
        <S.PlayerCardContainer>
          {user?.host && (
            <S.LeftTopMark src={images.waitingRoom.crown} alt="방장" />
          )}
          {!user?.host && user?.ready && (
            <S.LeftTopMark src={images.waitingRoom.ready} alt="레디" />
          )}
          <S.UserInfo>
            <S.TierFrame>
              <img className="frame" src={getTierImage(user.tier)} alt="티어" />
              <S.ProfileImage src={user.profileUrl} alt="프로필사진" />
            </S.TierFrame>

            <S.UserNickname>{user.nickname}</S.UserNickname>
          </S.UserInfo>
          {user.nickname !== myInfo?.nickname && (
            <S.UserDetailButton onClick={onClickDetailButton}>
              <img src={images.waitingRoom.info} alt="userInformation" />
            </S.UserDetailButton>
          )}
        </S.PlayerCardContainer>

        {/* 유저의 디테일한 정보를 보여주는 모달 */}
        <Modal isOpen={isOpen} closeModal={closeModal}>
          <UserDetailModal
            profileUrl={user.profileUrl}
            nickname={user?.nickname}
            isHost={isHost}
            handleKick={handleKick}
            closeModal={closeModal}
          />
        </Modal>
      </>
    ) : (
      // 비어 있는 칸이라면
      <S.EmptyPlayerCardContainer
        onClick={() => {
          if (isHost) onClickEmptySeat(index)
        }}
      ></S.EmptyPlayerCardContainer>
    )
  ) : (
    <S.EmptyPlayerCardContainer
      onClick={() => {
        if (isHost) onClickEmptySeat(index)
      }}
    >
      <S.InActiveMark src={images.waitingRoom.inactive} alt="잠김" />
    </S.EmptyPlayerCardContainer>
  )
}

export default PlayerCard
