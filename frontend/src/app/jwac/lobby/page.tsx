import withAuth from '@components/hoc/client/PrivateRoute'
import Lobby from '@components/lobby/Lobby'

const LobbyJWAC = () => {
  return <Lobby title="무제한 보석경매" />
}

export default withAuth(LobbyJWAC)
