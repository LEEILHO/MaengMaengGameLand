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
    color: black;
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
    color: black;
  `
}

export const RoundStartDisplayContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  border-radius: 8px;
  background: rgba(0, 0, 0, 0.75);
  box-shadow: 4px 8px 10px 0px rgba(0, 0, 0, 0.25);
`

export const Jewely = styled.img`
  width: 120px;
`

export const JewelyName = styled.p<JewelyTypeProps>`
  font-size: 32px;
  font-weight: 700;
  margin-left: 16px;

  ${({ $type }) => getColor($type)};
`

export const JewelyCost = styled.p<JewelyTypeProps>`
  font-size: 32px;
  font-weight: 700;
  ${({ $type }) => getGradientColor($type)};
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-left: 16px;
`
