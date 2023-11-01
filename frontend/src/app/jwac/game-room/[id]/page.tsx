'use client'

import * as S from '@styles/gameRoom/jwac/JWACRoom.styled'
import Lottie from 'lottie-react'
import Diamond from 'assets/lotties/emerald.json'
import { images } from '@constants/images'
import Timer from '@components/common/clients/Timer'
import CButton from '@components/common/clients/CButton'
import JWACUserList from '@components/gameRoom/jwac/JWACUserList'

const page = () => {
  return (
    <S.RoomContainer>
      <S.NewsContainer>
        <S.CumulativePrice>
          4 Round 기준 경매가 : 2억 4300만 2300원
        </S.CumulativePrice>
      </S.NewsContainer>
      <S.TimerContainer>
        <Timer size="80" fontSize="14" time={20} />
      </S.TimerContainer>
      <S.DisplayBoardContainer>
        <JWACUserList />
      </S.DisplayBoardContainer>
      <S.NoteContainer>
        <S.Discription>희망 낙찰가를 제시해주세요</S.Discription>
        <S.PriceRow>
          <S.PriceInput type="number" />
          <S.PriceUnit>원</S.PriceUnit>
        </S.PriceRow>
        <S.CumlativeAmountCotainer>
          <S.CumlativeDiscriptionRow>
            <img src={images.gameRoom.jwac.money} alt="누적 금액" />
            누적 사용 금액
          </S.CumlativeDiscriptionRow>
          <S.CumlativePrice>24억 5000만 2000원</S.CumlativePrice>
        </S.CumlativeAmountCotainer>
        <S.ButtonRow>
          <CButton
            text="제출"
            color="white"
            radius={24}
            backgroundColor="#7000FF"
            fontSize={14}
            height={32}
          />
        </S.ButtonRow>
      </S.NoteContainer>
      <S.ShowCaseCatainer>
        <S.Table src={images.gameRoom.jwac.table} alt="테이블" />
        <S.JewelContainer>
          <Lottie animationData={Diamond} loop />
        </S.JewelContainer>
      </S.ShowCaseCatainer>
    </S.RoomContainer>
  )
}

export default page
