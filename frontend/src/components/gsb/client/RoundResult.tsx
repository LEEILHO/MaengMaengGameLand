'use client'

import {
  AllBetChipsState,
  CurrentPlayerState,
  DisplayMessageState,
  GameOverState,
  MyState,
  OpponentState,
  ResultState,
  RoundState,
} from '@atom/gsbAtom'
import { userState } from '@atom/userAtom'
import { images } from '@constants/images'
import useModal from '@hooks/useModal'
import * as S from '@styles/gsb/RoundResult.styled'
import { CombResultType } from '@type/gsb/gsb.type'
import { useEffect, useState } from 'react'
import { useRecoilState, useRecoilValue, useSetRecoilState } from 'recoil'

const RoundResult = () => {
  const { isOpen, closeModal, Modal, openModal } = useModal()
  const [round, setRound] = useRecoilState(RoundState)
  const user = useRecoilValue(userState)
  const currentPlayer = useRecoilValue(CurrentPlayerState)
  const [displayMessage, setDisplayMessage] =
    useRecoilState(DisplayMessageState)
  const result = useRecoilValue(ResultState)
  const allBetChips = useRecoilValue(AllBetChipsState)
  const setMy = useSetRecoilState(MyState)
  const setOpponent = useSetRecoilState(OpponentState)
  const gameOver = useRecoilValue(GameOverState)

  const [myComb, setMyComb] = useState<CombResultType>({
    gold: 0,
    silver: 0,
    bronze: 0,
  })
  const [opponentComb, setOpponentComb] = useState<CombResultType>({
    gold: 0,
    silver: 0,
    bronze: 0,
  })

  const closeAndNext = () => {
    console.log('게임 종료가 되는지: ', gameOver)

    if (gameOver) {
      // 게임 종료
      setRound('GameOver')
      setDisplayMessage('게임이 종료되었습니다')
    } else {
      if (currentPlayer === user?.nickname) {
        setRound('Combination')
        setDisplayMessage('금은동을 조합해서 올려주세요')
      } else {
        setRound('CombWaiting')
        setDisplayMessage('상대방이 금은동을 조합합니다')
      }
      setMy((prev) => {
        if (!prev) return null
        return {
          ...prev,
          currentWeight: 0,
        }
      })
      setOpponent((prev) => {
        if (!prev) return null
        return {
          ...prev,
          currentWeight: 0,
        }
      })
    }
    closeModal()
  }

  useEffect(() => {
    console.log(round, '라운드의 결과')

    let timeId: NodeJS.Timeout
    if (result) {
      const win: CombResultType = {
        gold: result?.winnerGold,
        silver: result?.winnerSilver,
        bronze: result?.winnerBronze,
      }
      const lose: CombResultType = {
        gold: result.loserGold,
        silver: result.loserSilver,
        bronze: result.loserBronze,
      }
      if (result.winner === user?.nickname) {
        setMyComb(win)
        setOpponentComb(lose)
      } else {
        setMyComb(lose)
        setOpponentComb(win)
      }
    }

    if (round === 'DrawResult' || round === 'GiveUpResult') {
      // 포기했을 때는 모달 바로 뜨게
      openModal()
      timeId = setTimeout(() => {
        closeAndNext()
      }, 3000)
    } else {
      setTimeout(() => {
        if (result) {
          if (result.winner === user?.nickname) {
            setMy((prev) => {
              if (!prev) return null
              return {
                ...prev,
                currentChips: result.currentWinnerChips,
              }
            })
            setOpponent((prev) => {
              if (!prev) return null
              return {
                ...prev,
                currentChips: result.currentLoserChips,
              }
            })
          } else {
            setMy((prev) => {
              if (!prev) return null
              return {
                ...prev,
                currentChips: result.currentLoserChips,
              }
            })
            setOpponent((prev) => {
              if (!prev) return null
              return {
                ...prev,
                currentChips: result.currentWinnerChips,
              }
            })
          }
        }

        openModal()
        timeId = setTimeout(() => {
          closeAndNext()
        }, 3000)
      }, 5000)
    }

    return () => {
      clearTimeout(timeId)
    }
  }, [])

  // framer motion variants
  const combAreaVariants = {
    hidden: { opacity: 1, scale: 0 },
    visible: {
      opacity: 1,
      scale: 1,
      transition: {
        delayChildren: 1,
        staggerChildren: 1,
      },
    },
  }

  const itemVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
    },
  }

  return (
    <S.RoundResultContainer>
      {/* 포기 안 했을 때 */}
      {round === 'Result' && (
        <>
          <S.CombArea
            variants={combAreaVariants}
            initial="hidden"
            animate="visible"
          >
            <S.StarList variants={itemVariants}>
              {[...Array(myComb.bronze)].map((_, index) => (
                <S.Star
                  key={`bronze${index}`}
                  src={images.gsb.bronzeStar}
                  index={index}
                  reverse={'false'}
                />
              ))}
            </S.StarList>
            <S.StarList variants={itemVariants}>
              {[...Array(myComb.silver)].map((_, index) => (
                <S.Star
                  key={`silver${index}`}
                  src={images.gsb.silverStar}
                  index={index}
                  reverse={'false'}
                />
              ))}
            </S.StarList>
            <S.StarList variants={itemVariants}>
              {[...Array(myComb.gold)].map((_, index) => (
                <S.Star
                  key={`gold${index}`}
                  src={images.gsb.goldStar}
                  index={index}
                  reverse={'false'}
                />
              ))}
            </S.StarList>
          </S.CombArea>

          <S.CombArea
            variants={combAreaVariants}
            initial="hidden"
            animate="visible"
          >
            <S.StarList variants={itemVariants}>
              {[...Array(opponentComb.bronze)].map((_, index) => (
                <S.Star
                  key={index}
                  src={images.gsb.bronzeStar}
                  index={index}
                  reverse={'true'}
                />
              ))}
            </S.StarList>
            <S.StarList variants={itemVariants}>
              {[...Array(opponentComb.silver)].map((_, index) => (
                <S.Star
                  key={index}
                  src={images.gsb.silverStar}
                  index={index}
                  reverse={'true'}
                />
              ))}
            </S.StarList>
            <S.StarList variants={itemVariants}>
              {[...Array(opponentComb.gold)].map((_, index) => (
                <S.Star
                  key={index}
                  src={images.gsb.goldStar}
                  index={index}
                  reverse={'true'}
                />
              ))}
            </S.StarList>
          </S.CombArea>
        </>
      )}

      <Modal isOpen={isOpen}>
        <S.RoundResultModalContainer>
          {round === 'GiveUpResult' && (
            <S.ModalContent>{displayMessage}</S.ModalContent>
          )}
          {round === 'DrawResult' && (
            <S.ModalContent>
              비겼습니다. <br />
              모인 칩이 다음 라운드로 이월됩니다.
            </S.ModalContent>
          )}
          {round === 'Result' && (
            <S.ModalContent>
              {result?.winner}님이 {allBetChips}개의 칩을 가져갑니다.
            </S.ModalContent>
          )}
        </S.RoundResultModalContainer>
      </Modal>
    </S.RoundResultContainer>
  )
}

export default RoundResult
