'use client'

import CButton from '@components/common/clients/CButton'
import Timer from '@components/common/clients/Timer'
import useModal from '@hooks/useModal'
import { useRouter } from 'next/navigation'
import { styled } from 'styled-components'
import { motion } from 'framer-motion'
import { useState, useEffect } from 'react'
import JWACResult from '@components/gameRoom/jwac/JWACResult'
import YouTube, { YouTubeProps } from 'react-youtube'

const Test = () => {
  const router = useRouter()
  const { Modal, isOpen, openModal, closeModal } = useModal()
  const [color, setColor] = useState('red')

  const onPlayerReady: YouTubeProps['onReady'] = (event) => {
    // access to player in all event handlers via event.target
    event.target.pauseVideo()
  }

  const opts: YouTubeProps['opts'] = {
    height: '390',
    width: '640',
    playerVars: {
      // https://developers.google.com/youtube/player_parameters
      autoplay: 1,
      rel: 0,
      modestbranding: 1,
    },
  }

  useEffect(() => {
    console.log(color)
  }, [color])

  return (
    <>
      <StyledTestContainer>
        <YouTube videoId="ArLppdaT_Pw" opts={opts} onReady={onPlayerReady} />
        {/* <CButton
          radius={36}
          backgroundColor="rgba(112, 0, 255, 1)"
          text="테스트 버튼"
          fontSize={16}
          color="white"
          onClick={() => openModal()}
        ></CButton>

        <motion.div
          style={{
            width: '100%',
            height: '100%',
          }}
          // layout
        >
          {color === 'red' && (
            <motion.div
              style={{
                width: '100%',
                height: '100%',
              }}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }} // 라운드 종료 시만 보이도록
              onClick={() => setColor('blue')}
              layoutId="red"
            >
              <TestRed />
            </motion.div>
          )}
          {color === 'blue' && (
            <motion.div
              style={{
                width: '100%',
                height: '100%',
              }}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }} // 라운드 종료 시만 보이도록
              onClick={() => setColor('green')}
              layoutId="blue"
            >
              <TestBlue />
            </motion.div>
          )}
          {color === 'green' && (
            <motion.div
              style={{
                width: '100%',
                height: '100%',
              }}
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }} // 라운드 종료 시만 보이도록
              layoutId="green"
            >
              <TestGreen />
            </motion.div>
          )}
        </motion.div> */}

        {/* <Timer size="150" fontSize="24" time={10} /> */}
        {/* <JWACResult /> */}
      </StyledTestContainer>
    </>
  )
}

export default Test

const StyledTestContainer = styled.div`
  position: fixed;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: purple;
`

const TestRed = styled.div`
  width: 100%;
  height: 100%;
  background-color: red;
`

const TestBlue = styled.div`
  width: 100%;
  height: 100%;
  background-color: blue;
`
const TestGreen = styled.div`
  width: 100%;
  height: 100%;
  background-color: green;
`
