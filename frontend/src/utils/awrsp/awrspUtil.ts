import { images } from '@constants/images'
import { RspType } from '@type/awrsp/awrsp.type'

export const getRspImageUrl = (type: RspType) => {
  if (type === 'ROCK') return images.awrsp.rockCard
  if (type === 'SCISSOR') return images.awrsp.scissorsCard
  if (type === 'PAPER') return images.awrsp.paperCard
  if (type === 'DRAW_ROCK') return images.awrsp.rockDrawCard
  if (type === 'DRAW_SCISSOR') return images.awrsp.scissorsDrawCard
  if (type === 'DRAW_PAPER') return images.awrsp.paperDrawCard
}
