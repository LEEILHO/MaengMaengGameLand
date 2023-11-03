'use client'

import useAxiosInterceptor from '@hooks/useAxiosInterceptor'
import { NextPage } from 'next'
import React from 'react'

const withAuth = (Component: NextPage) => {
  const Auth = () => {
    useAxiosInterceptor()

    return <Component />
  }

  return Auth
}

export default withAuth
