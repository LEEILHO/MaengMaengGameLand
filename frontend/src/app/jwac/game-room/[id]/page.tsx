'use client'

import * as S from '@styles/gameRoom/jwac/JWACRoom.styled'
import Lottie from 'lottie-react'
import Diamond from 'assets/lotties/emerald.json'
import { images } from '@constants/images'
import Timer from '@components/common/clients/Timer'
import CButton from '@components/common/clients/CButton'
import JWACUserList from '@components/gameRoom/jwac/JWACUserList'
import { useState, useMemo, useEffect, useCallback } from 'react'
import { useRecoilValue } from 'recoil'
import {
  jwacPlayerListState,
  jwacRoundResultState,
  jwacRoundState,
} from '@atom/jwacAtom'
import { formatKoreanCurrency, jewelryToLottie } from '@utils/gameRoom/jwacUtil'
import { userState } from '@atom/userAtom'
import { usePathname } from 'next/navigation'
import useSocketJWAC from '@hooks/useSocketJWAC'
import JWACRoundStartDisplay from '@components/gameRoom/jwac/JWACRoundStartDisplay'
import JWACRoundResultDisplay from '@components/gameRoom/jwac/JWACRoundResultDisplay'
import useModal from '@hooks/useModal'
import JewelryInfomationModal from '@components/gameRoom/jwac/JewelryInfomationModal'
import useDidMountEffect from '@hooks/useDidMoundEffect'

const page = () => {
  const {
    connectSocket,
    disconnectSocket,
    handleBid,
    timeOver,
    roundData,
    roundResult,
    playerList: players,
  } = useSocketJWAC()
  const { Modal, isOpen, closeModal, openModal } = useModal()
  const pathname = usePathname().split('game-room/')[1]
  const [isLoading, setIsLoading] = useState(true) // 사람들이 모두 들어오기 전에 로딩 페이지를 보여줄지 말지
  const [isRoundStart, setIsRoundStart] = useState(false)
  const [isRoundEnd, setIsRoundEnd] = useState(false)
  const [bidMoney, setBidMoney] = useState(0)
  const user = useRecoilValue(userState)
  const myData = useMemo(
    () => players.filter((player) => player.nickname === user?.nickname)[0],
    [players, user],
  ) // 현재 플레이어의 정보
  const prevRound = useMemo(() => {
    if (!roundResult) return 0
    return Math.floor(roundResult.round / 4)
  }, [roundResult])

  const handleTimeOver = useCallback(() => {
    if (user) {
      timeOver(pathname, user.nickname)
    }
  }, [pathname])

  const handleBidMody = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      setBidMoney(Number(e.target.value))
    },
    [],
  )

  useEffect(() => {
    console.log('[플레이어 정보 변동]', players)
    console.log('[내 정보]', myData)
  }, [players, myData])

  useEffect(() => {
    if (user) {
      connectSocket(pathname, user.nickname)
    }
    return () => {
      disconnectSocket()
    }
  }, [pathname, user])

  useEffect(() => {
    console.log('[라운드 데이터 변경]', roundData)
  }, [roundData])

  // 라운드가 시작되고 3초간 경매 보석 정보 보여주기
  useDidMountEffect(() => {
    setIsRoundStart(true)
    // 3초 후에 isRoundStart를 false로 변경
    const timeoutId = setTimeout(() => {
      setIsRoundStart(false)
    }, 3000) // 3초를 밀리초로 설정

    return () => {
      clearTimeout(timeoutId) // cleanup 시 clearTimeout을 호출하여 타이머를 제거
    }
  }, [roundData])
  // 라운드가 끝나고 3초간 경매 결과 보여주기
  useDidMountEffect(() => {
    setIsRoundEnd(true)
    const timeoutId = setTimeout(() => {
      setIsRoundEnd(false)
    }, 1500)

    return () => {
      clearTimeout(timeoutId)
    }
  }, [roundResult])

  if (!user || !roundData || !players) return <div>loading...</div>

  return (
    <>
      <Modal isOpen={isOpen} closeModal={closeModal}>
        <JewelryInfomationModal />
      </Modal>
      <S.RoomContainer>
        <S.NewsContainer>
          {roundResult && (
            <S.CumulativePrice>
              {`${prevRound} Round 기준 경매가 : ${formatKoreanCurrency(
                roundResult.roundBidSum,
              )}`}
            </S.CumulativePrice>
          )}
        </S.NewsContainer>
        {myData.item && (
          <S.ItemContainer onClick={openModal}>
            <S.ItemIcon src={images.gameRoom.jwac.checkIcon} alt="보석확인권" />
          </S.ItemContainer>
        )}
        <S.TimerContainer>
          <Timer
            size="80"
            fontSize="14"
            time={roundData.time}
            round={roundData.round}
            timeOverHandle={handleTimeOver}
          />
        </S.TimerContainer>
        <S.DisplayBoardContainer>
          <S.DisplayRoundFrame src={images.gameRoom.jwac.roundFrame} />
          <S.RoundText>{`${roundData.round} Round`}</S.RoundText>
          {isRoundStart && (
            <JWACRoundStartDisplay
              jewely={roundData.jewelry}
              socre={roundData.jewelryScore}
            />
          )}
          {!isRoundStart && isRoundEnd && (
            <JWACRoundResultDisplay
              jewely={roundData.jewelry}
              socre={roundData.jewelryScore}
              roundResult={roundResult}
            />
          )}
          {!isRoundStart && !isRoundEnd && <JWACUserList players={players} />}
        </S.DisplayBoardContainer>
        <S.NoteContainer>
          <S.Discription>희망 낙찰가를 제시해주세요</S.Discription>
          <S.PriceRow>
            <S.PriceInput
              type="number"
              value={bidMoney.toString()}
              onChange={handleBidMody}
            />
            <S.PriceUnit>원</S.PriceUnit>
          </S.PriceRow>
          <S.CumlativeAmountCotainer>
            <S.CumlativeDiscriptionRow>
              <img src={images.gameRoom.jwac.money} alt="누적 금액" />
              누적 사용 금액
            </S.CumlativeDiscriptionRow>
            <S.CumlativePrice>
              {formatKoreanCurrency(myData.bidSum)}
            </S.CumlativePrice>
          </S.CumlativeAmountCotainer>
          <S.ButtonRow>
            <CButton
              text="제출"
              color="white"
              radius={24}
              backgroundColor="#7000FF"
              fontSize={14}
              height={32}
              onClick={() => handleBid(pathname, user.nickname, bidMoney)}
            />
          </S.ButtonRow>
        </S.NoteContainer>
        <S.ShowCaseCatainer>
          <S.Table src={images.gameRoom.jwac.table} alt="테이블" />
          <S.JewelContainer>
            <Lottie animationData={jewelryToLottie(roundData.jewelry)} loop />
          </S.JewelContainer>
        </S.ShowCaseCatainer>
      </S.RoomContainer>
    </>
  )
}

export default page
