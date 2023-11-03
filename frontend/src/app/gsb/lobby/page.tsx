'use client'

import withAuth from '@components/hoc/client/PrivateRoute'
import Lobby from '@components/lobby/Lobby'

const LobbyGSB = () => {
  return <Lobby title="금은동" />
}

export default withAuth(LobbyGSB)
