'use client'

import { images } from '@constants/images'
import * as S from '@styles/gsb/RoundResult.styled'

const RoundResult = () => {
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

  const gold = [...Array(1)]
  const silver = [...Array(3)]
  const bronze = [...Array(12)]
  return (
    <S.RoundResultContainer>
      <S.CombArea
        variants={combAreaVariants}
        initial="hidden"
        animate="visible"
      >
        <S.StarList variants={itemVariants}>
          {bronze.map((_, index) => (
            <S.Star
              key={`bronze${index}`}
              src={images.gsb.bronzeStar}
              index={index}
              reverse={'false'}
            />
          ))}
        </S.StarList>
        <S.StarList variants={itemVariants}>
          {silver.map((_, index) => (
            <S.Star
              key={`silver${index}`}
              src={images.gsb.silverStar}
              index={index}
              reverse={'false'}
            />
          ))}
        </S.StarList>
        <S.StarList variants={itemVariants}>
          {gold.map((_, index) => (
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
          {bronze.map((_, index) => (
            <S.Star
              key={index}
              src={images.gsb.bronzeStar}
              index={index}
              reverse={'true'}
            />
          ))}
        </S.StarList>
        <S.StarList variants={itemVariants}>
          {silver.map((_, index) => (
            <S.Star
              key={index}
              src={images.gsb.silverStar}
              index={index}
              reverse={'true'}
            />
          ))}
        </S.StarList>
        <S.StarList variants={itemVariants}>
          {gold.map((_, index) => (
            <S.Star
              key={index}
              src={images.gsb.goldStar}
              index={index}
              reverse={'true'}
            />
          ))}
        </S.StarList>
      </S.CombArea>
    </S.RoundResultContainer>
  )
}

export default RoundResult
