'use client'

import { accessTokenState } from '@atom/userAtom'
import { ResponseAccessTokenType } from '@type/common/auth.type'
import { http, authAxios } from '@utils/http'
import {
  AxiosError,
  AxiosRequestHeaders,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from 'axios'
import { useEffect } from 'react'
import { useRecoilState } from 'recoil'
import useInitUser from './useInitUser'

const useAxiosInterceptor = () => {
  const [accessToken, setAccessToken] = useRecoilState(accessTokenState)

  const errorHandler = (error: AxiosError) => {
    console.log('errInterceptor!', error)
    if (error.response?.status === 401) {
      // window.location.href = '/login'
    }
    return Promise.reject(error)
  }

  const requestHandler = async (config: InternalAxiosRequestConfig) => {
    if (accessToken?.accessToken) {
      config.headers = config.headers || {}
      ;(config.headers as AxiosRequestHeaders).Authorization = accessToken
        ? `Bearer ${accessToken.accessToken}`
        : ''
    }

    // 엑세스토큰 없는 경우 리프레시 토큰으로 엑세스 토큰 재발급
    else {
      try {
        const refreshResponse =
          await http.get<ResponseAccessTokenType>('auth/token')
        const newAccessToken = refreshResponse.accessToken

        if (newAccessToken) {
          config.headers = config.headers || {}
          ;(
            config.headers as AxiosRequestHeaders
          ).Authorization = `Bearer ${newAccessToken}`

          setAccessToken(refreshResponse)
          useInitUser()
        }
      } catch (error) {
        console.error('엑세스 토큰 재발급 실패: ', error)
        // 이후 로그인 화면으로 이동시키기
        // window.location.href = '/login'
      }
    }
    return config
  }
  const responseHandler = <T,>(
    response: AxiosResponse<T>,
  ): AxiosResponse<T> => {
    return response
  }
  const requestInterceptor = authAxios.interceptors.request.use(requestHandler)

  const responseInterceptor = authAxios.interceptors.response.use(
    (response: AxiosResponse) => responseHandler(response),
    (error: AxiosError) => errorHandler(error),
  )

  useEffect(() => {
    return () => {
      authAxios.interceptors.request.eject(requestInterceptor)
      authAxios.interceptors.response.eject(responseInterceptor)
    }
  }, [responseInterceptor, requestInterceptor])
}

export default useAxiosInterceptor
