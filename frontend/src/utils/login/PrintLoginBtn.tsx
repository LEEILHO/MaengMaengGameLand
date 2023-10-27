import * as S from '@styles/login/Login.styled'
import LoginButton from '@components/login/client/LoginButton'

type Props = {
  isLogin: boolean | null
}

export const PrintLoginBtn = ({ isLogin }: Props) => {
  if (isLogin === null) {
    return <S.Announcement>잠시만 기다려주세요.</S.Announcement>
  } else if (!isLogin) {
    return (
      <>
        <S.Announcement>계속하시려면 로그인을 해주세요.</S.Announcement>
        <LoginButton />
      </>
    )
  } else {
    return <S.Announcement>계속하려면 클릭해주세요.</S.Announcement>
  }
}
