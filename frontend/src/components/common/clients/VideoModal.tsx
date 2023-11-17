import YouTube, { YouTubeProps } from 'react-youtube'
import * as S from '@styles/common/VideoModal.styled'
import { images } from '@constants/images'
import { soundState } from '@atom/soundAtom'
import { useRecoilState } from 'recoil'
import { useEffect } from 'react'

type Props = {
  closeModal: () => void
  title: string
}

const VideoModal = ({ closeModal, title }: Props) => {
  const [isSound, setIsSound] = useRecoilState(soundState)
  const prevSound = isSound?.bgmSound

  const onPlayerReady: YouTubeProps['onReady'] = (event) => {
    // access to player in all event handlers via event.target
    event.target.pauseVideo()
  }

  const getVideoId = () => {
    if (title === '금은동') {
      return '7GhGIvcjFzM'
    }
    if (title === '무제한 보석 경매') {
      return '4AqMwXQ05W4'
    }
    if (title === '전승 가위바위보') {
      return '2L11dPMdEIg'
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

  // 모달 키면 배경소리 껐다가 끄면 다시 키기
  useEffect(() => {
    setIsSound((prev) => {
      return {
        bgmSound: false,
        effectSound: prev?.effectSound === null ? true : prev!.effectSound,
      }
    })

    return () => {
      setIsSound((prev) => {
        return {
          bgmSound: prevSound ? prevSound : true,
          effectSound: prev?.effectSound === null ? true : prev!.effectSound,
        }
      })
    }
  }, [])

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
