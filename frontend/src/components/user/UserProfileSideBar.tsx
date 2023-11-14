import { userState } from '@atom/userAtom'
import { images } from '@constants/images'
import * as S from '@styles/user/UserProfileSideBar.styled'
import { useRouter } from 'next/navigation'
import { useRecoilState } from 'recoil'
import { useState, useRef } from 'react'
import { editProfileImage, editUser } from 'apis/user/userApi'

const UserProfileSideBar = () => {
  const router = useRouter()
  const [user, setUser] = useRecoilState(userState)
  const selectFileRef = useRef<HTMLInputElement | null>(null)
  const [isEdit, setIsEdit] = useState(false)
  const [userName, setUserName] = useState('')
  const [profileImage, setProfileImage] = useState<string | null>(null)

  const handleEdit = () => {
    setUserName(user!.nickname)
    setIsEdit(true)
  }

  const handleSubmit = () => {
    setUser({ ...user!, nickname: userName })
    setIsEdit(false)
    editUser(userName)
  }

  const handleUserName = (e: React.ChangeEvent<HTMLInputElement>) => {
    setUserName(e.target.value)
  }

  const handleProfileImage = async () => {
    if (selectFileRef.current?.files) {
      const file = selectFileRef.current?.files[0]
      const reader = new FileReader()
      reader.readAsDataURL(file)
      reader.onloadend = () => {
        setProfileImage(reader.result as string)
      }
      const formData = new FormData()
      formData.append('profileImage', file)
      editProfileImage(formData)
    }
  }

  return (
    <S.UserProfileSideBarContainer>
      <S.UserProfileBox onClick={() => selectFileRef.current?.click()}>
        <S.UserProfileImage
          src={profileImage ? profileImage! : user?.profile}
          alt="유저프로필"
        />
        <input
          type="file"
          style={{ display: 'none' }}
          onChange={handleProfileImage}
          ref={selectFileRef}
        />
      </S.UserProfileBox>

      <S.UserNameRow>
        {!isEdit ? (
          <>
            <S.UserName>{user?.nickname}</S.UserName>
            <S.UserNameEditIcon
              src={images.user.edit}
              alt="수정"
              onClick={handleEdit}
            />
          </>
        ) : (
          <S.UserNameInputContainer>
            <S.UserNameInput
              value={userName}
              onChange={handleUserName}
              maxLength={12}
            />
            <S.UserNameSubmitIcon
              src={images.user.submit}
              alt="확인"
              onClick={handleSubmit}
            />
          </S.UserNameInputContainer>
        )}
      </S.UserNameRow>
      <S.GoBackIcon
        src={images.user.back}
        alt="뒤로가기"
        onClick={() => router.replace('/home')}
      />
    </S.UserProfileSideBarContainer>
  )
}

export default UserProfileSideBar
