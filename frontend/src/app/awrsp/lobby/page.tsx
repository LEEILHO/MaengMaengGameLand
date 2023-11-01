'use client'

import withAuth from '@components/hoc/client/PrivateRoute'
import Lobby from '@components/lobby/Lobby'

const LobbyAWRSP = () => {
  return <Lobby title="전승 가위바위보" />
}

export default withAuth(LobbyAWRSP)
