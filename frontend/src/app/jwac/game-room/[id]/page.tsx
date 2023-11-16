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
  extractNumberFromString,
  formatKoreanCurrency,
  jewelryToLottie,
} from '@utils/gameRoom/jwacUtil'
import { userState } from '@atom/userAtom'
import { usePathname, useRouter } from 'next/navigation'
import useSocketJWAC from '@hooks/useSocketJWAC'
import JWACRoundStartDisplay from '@components/gameRoom/jwac/JWACRoundStartDisplay'
import JWACRoundResultDisplay from '@components/gameRoom/jwac/JWACRoundResultDisplay'
import useModal from '@hooks/useModal'
import JewelryInfomationModal from '@components/gameRoom/jwac/JewelryInfomationModal'
import useDidMountEffect from '@hooks/useDidMoundEffect'
import JWACResult from '@components/gameRoom/jwac/JWACResult'
import { NumericFormat } from 'react-number-format'
import useSound from '@hooks/useSound'

const page = () => {
  const {
    connectSocket,
    disconnectSocket,
    handleBid,
    timeOver,
    roundData,
    roundResult,
    playerList: players,
    isGameEnd,
    gameResult,
  } = useSocketJWAC()
  const pathname = usePathname().split('game-room/')[1]
  const router = useRouter()
  const { Modal, isOpen, closeModal, openModal } = useModal()
  const { playWriteSound, playButtonSound } = useSound()
  const [isLoading, setIsLoading] = useState(true) // 사람들이 모두 들어오기 전에 로딩 페이지를 보여줄지 말지
  const [isRoundStart, setIsRoundStart] = useState(false)
  const [isRoundEnd, setIsRoundEnd] = useState(false)
  const [isSubmit, setIsSubmit] = useState(false)
  const [bidMoney, setBidMoney] = useState(0)
  const [submitMoney, setSubmitMoney] = useState(0)
  const user = useRecoilValue(userState)
  const myData = useMemo(
    () => players.filter((player) => player.nickname === user?.nickname)[0],
    [players, user],
  ) // 현재 플레이어의 정보
  const prevRound = useMemo(() => {
    if (!roundResult) return 0
    return Math.floor(roundResult.round / 4) * 4
  }, [roundResult])

  const handleTimeOver = useCallback(() => {
    if (user) {
      timeOver(pathname, user.nickname)
    }
  }, [pathname])

  const handleBidMody = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      console.log('숫자 바뀜', e.target.value)

      setBidMoney(extractNumberFromString(e.target.value))
    },
    [],
  )

  const handleSubmit = () => {
    playWriteSound()
    if (user) {
      handleBid(pathname, user.nickname, bidMoney)
    }
    setIsSubmit(true)
    setSubmitMoney(bidMoney)
    setBidMoney(0)
  }

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
    console.log(bidMoney)
  }, [bidMoney])

  // 다음 라운드 시작 시
  useEffect(() => {
    console.log('[라운드 데이터 변경]', roundData)
    setIsSubmit(false)
  }, [roundData])

  // 라운드가 시작되고 3초간 경매 보석 정보 보여주기
  useDidMountEffect(() => {
    setIsRoundStart(true)
    // 3초 후에 isRoundStart를 false로 변경
    const timeoutId = setTimeout(() => {
      setIsRoundStart(false)
    }, 10000) // 3초를 밀리초로 설정

    return () => {
      clearTimeout(timeoutId) // cleanup 시 clearTimeout을 호출하여 타이머를 제거
    }
  }, [roundData])
  // 라운드가 끝나고 1.5초간 경매 결과 보여주기
  useDidMountEffect(() => {
    console.log('[라운드 종료]')
    setIsRoundEnd(true)
    const timeoutId = setTimeout(() => {
      setIsRoundEnd(false)
    }, 5000)

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
        {!isGameEnd && (
          <>
            <S.NewsContainer>
              {roundResult && (
                <S.CumulativePrice>
                  {prevRound !== 0
                    ? `${prevRound}Round 기준 경매가 : ${formatKoreanCurrency(
                        roundResult.roundBidSum,
                      )}`
                    : '4Round마다 누적 라운드 금액이 공개됩니다.'}
                </S.CumulativePrice>
              )}
            </S.NewsContainer>
            {myData.item && (
              <S.ItemContainer onClick={openModal}>
                <S.ItemIcon
                  src={images.gameRoom.jwac.checkIcon}
                  alt="보석확인권"
                />
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
              {!isRoundEnd && isRoundStart && (
                <JWACRoundStartDisplay
                  jewely={roundData.jewelry}
                  socre={roundData.jewelryScore}
                />
              )}
              {isRoundEnd && roundResult && (
                <JWACRoundResultDisplay
                  jewely={roundResult.jewelry}
                  socre={roundResult.jewelryScore}
                  roundResult={roundResult}
                />
              )}
              {!isRoundStart && !isRoundEnd && (
                <JWACUserList players={players} />
              )}
            </S.DisplayBoardContainer>
            <S.NoteContainer>
              <S.Discription>희망 낙찰가를 제시해주세요</S.Discription>
              {isSubmit ? (
                <>
                  <S.PriceRow>
                    <S.SubmitPrice>{`제출금액 : ${formatKoreanCurrency(
                      submitMoney,
                    )}원`}</S.SubmitPrice>
                  </S.PriceRow>
                </>
              ) : (
                <>
                  <S.PriceRow>
                    <NumericFormat
                      value={bidMoney}
                      thousandSeparator=","
                      allowNegative={false}
                      decimalScale={0}
                      suffix="원"
                      isAllowed={(values) => {
                        let { floatValue } = values
                        if (!floatValue) floatValue = 0
                        return floatValue < 1000000000000000
                      }}
                      onChange={handleBidMody}
                      className="price-input"
                    />
                  </S.PriceRow>
                  <S.CurrentPriceRow>
                    {formatKoreanCurrency(bidMoney)}
                  </S.CurrentPriceRow>
                </>
              )}

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
                {isSubmit ? (
                  <CButton
                    text="제출 완료"
                    color="white"
                    radius={24}
                    backgroundColor="#bababa"
                    fontSize={14}
                    height={32}
                    onClick={() => {}}
                    disabled
                  />
                ) : (
                  <CButton
                    text="제출"
                    color="white"
                    radius={24}
                    backgroundColor="#7000FF"
                    fontSize={14}
                    height={32}
                    onClick={handleSubmit}
                  />
                )}
              </S.ButtonRow>
            </S.NoteContainer>
            <S.ShowCaseCatainer>
              <S.Table src={images.gameRoom.jwac.table} alt="테이블" />
              <S.JewelContainer>
                <Lottie
                  animationData={jewelryToLottie(roundData.jewelry)}
                  loop
                />
              </S.JewelContainer>
            </S.ShowCaseCatainer>
          </>
        )}

        {/* 결과화면 렌더링 */}
        {isGameEnd && (
          <S.RoomResultContainer>
            <JWACResult gameResult={gameResult} />
            <S.BackButton
              src={images.gameRoom.jwac.backWhite}
              alt="로비로 나가기"
              onClick={() => {
                router.replace('/jwac/lobby')
              }}
            />
          </S.RoomResultContainer>
        )}
      </S.RoomContainer>
    </>
  )
}

export default page
