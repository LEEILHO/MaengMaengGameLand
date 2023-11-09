import { JewelryType } from '@type/gameRoom/jwac.type'
import { styled } from 'styled-components'
import css from 'styled-jsx/css'

type JewelyTypeProps = {
  $type: JewelryType
}

const getColor = (type: JewelryType) => {
  if (type === 'DIAMOND') {
    return `
      color: #e4f1ff;
    `
  }
  if (type === 'RUBY') {
    return `
      color: #d93b57;
    `
  }
  if (type === 'SAPPHIRE') {
    return `
      color: #1e6bbd;
    `
  }
  if (type === 'EMERALD') {
    return `
      color: #0db11d;
    `
  }
  return `
    color: white;
  `
}

const getGradientColor = (type: JewelryType) => {
  if (type === 'DIAMOND') {
    return `
      background: linear-gradient(180deg, #1787FF 0%, #FFF 50%, #3395FF 100%);
    `
  }
  if (type === 'RUBY') {
    return `
      background: linear-gradient(180deg, #9A4617 0%, #FFF 50%, #9A4617 100%);
    `
  }
  if (type === 'SAPPHIRE') {
    return `
      background: linear-gradient(180deg, #122AA7 0%, #FFF 50%, #122AA7 100%);
    `
  }
  if (type === 'EMERALD') {
    return `
      background: linear-gradient(180deg, #175911 0%, #FFF 50%, #175911 100%);
    `
  }
  return `
    color: white;
  `
}

export const RoundResultDisplayContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  border-radius: 8px;
  background: rgba(0, 0, 0, 0.75);
  box-shadow: 4px 8px 10px 0px rgba(0, 0, 0, 0.25);
  gap: 20px;
`

export const JewelyDataRow = styled.div`
  display: flex;
  margin-bottom: 12px;
  align-items: center;
  justify-content: center;
`

export const DisplayRow = styled.div`
  display: flex;
  width: 100%;
`

export const Jewely = styled.img`
  width: 80px;
`

export const JewelyName = styled.p<JewelyTypeProps>`
  font-size: 28px;
  font-weight: 700;
  margin-left: 16px;

  ${({ $type }) => getColor($type)};
`

export const JewelyCost = styled.p<JewelyTypeProps>`
  font-size: 28px;
  font-weight: 700;
  ${({ $type }) => getGradientColor($type)};
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-left: 16px;
`

export const SuccessfulBidder = styled.p`
  color: #aed2ff;
  font-size: 24px;
  font-weight: 500;
  text-align: center;
  flex-basis: 50%;
`

export const SuccessfulBidderName = styled.p`
  font-size: 24px;
  font-weight: 500;
  color: white;
  margin-right: 12px;
`

export const SuccessfulBidderScore = styled.p`
  font-size: 24px;
  font-weight: 500;
  color: white;
`

export const LowestPriceBidder = styled.p`
  color: #aed2ff;
  font-size: 24px;
  font-weight: 500;
  text-align: center;
  flex-basis: 50%;
`

export const LowestPriceBidderName = styled.p`
  font-size: 24px;
  font-weight: 500;
  color: white;
  margin-right: 12px;
`

export const LowestPriceBidderScore = styled.p`
  font-size: 24px;
  font-weight: 500;
  color: white;
`
