'use client'

import withAuth from '@components/hoc/client/PrivateRoute'
import React from 'react'

import WaitingRoomPage from '@components/waitingRoom/client/WaitingRoomPage'

const WaitingRoom = () => {
  return <WaitingRoomPage />
}

export default withAuth(WaitingRoom)
