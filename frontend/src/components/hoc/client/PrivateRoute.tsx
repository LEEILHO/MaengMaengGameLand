'use client'

import useAxiosInterceptor from '@hooks/useAxiosInterceptor'
import useInitUser from '@hooks/useInitUser'
import { NextPage } from 'next'
import React from 'react'

const withAuth = (Component: NextPage) => {
  const initUser = useInitUser()
  const Auth = () => {
    useAxiosInterceptor()
    initUser()

    return <Component />
  }

  return Auth
}

export default withAuth
