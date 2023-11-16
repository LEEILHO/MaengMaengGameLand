import YouTube, { YouTubeProps } from 'react-youtube'
import * as S from '@styles/common/VideoModal.styled'
import { images } from '@constants/images'

type Props = {
  closeModal: () => void
  title: string
}

const VideoModal = ({ closeModal, title }: Props) => {
  const onPlayerReady: YouTubeProps['onReady'] = (event) => {
    // access to player in all event handlers via event.target
    event.target.pauseVideo()
  }

  const getVideoId = () => {
    if (title === '금은동') {
      return ''
    }
    if (title === '무제한 보석 경매') {
      return 'ArLppdaT_Pw'
    }
    if (title === '전승 가위바위보') {
      return ''
    }
  }

  const opts: YouTubeProps['opts'] = {
    height: '250',
    width: '445',
    playerVars: {
      // https://developers.google.com/youtube/player_parameters
      autoplay: 1,
      rel: 0,
      modestbranding: 1,
    },
  }

  return (
    <S.VideoModalContainer>
      <S.TopRow>
        <S.Title>{title}</S.Title>
        <S.CloseIcon src={images.lobby.close} alt="닫기" onClick={closeModal} />
      </S.TopRow>
      <YouTube videoId={getVideoId()} opts={opts} onReady={onPlayerReady} />
    </S.VideoModalContainer>
  )
}

export default VideoModal
