// import { setToken } from '@store/auth';
// import store from '@store/store';
// import { AuthType } from '@typedef/common.types';
// eslint-disable-next-line import/no-named-as-default
// import { ResponseAccessTokenType } from '@/types/common/auth.type';
import AxiosS, {
  // eslint-disable-next-line import/named
  AxiosRequestConfig,
} from 'axios'

const axios = AxiosS.create()
const gameAxios = AxiosS.create()
export const authAxios = AxiosS.create()
axios.defaults.baseURL = 'https://k9d208.p.ssafy.io/api/'
axios.defaults.withCredentials = true
authAxios.defaults.baseURL = 'http://localhost:8000'
authAxios.defaults.withCredentials = true
gameAxios.defaults.baseURL = 'http://j9d207.p.ssafy.io:8081/api/'
gameAxios.defaults.withCredentials = true

export const http = {
  get: async function get<Response = unknown>(
    url: string,
    header?: AxiosRequestConfig['headers'],
  ) {
    const options: AxiosRequestConfig = {
      headers: header,
    }

    const res = await axios.get<Response>(url, options)
    return res.data
  },
  post: async function post<Response = unknown, Request = unknown>(
    url: string,
    body?: Request,
    header?: AxiosRequestConfig['headers'],
  ) {
    const options: AxiosRequestConfig = {
      headers: header,
    }

    const res = await axios.post<Response>(url, body, options)
    return res.data
  },
}

export const gameHttp = {
  get: async function get<Response = unknown>(
    url: string,
    header?: AxiosRequestConfig['headers'],
  ) {
    const options: AxiosRequestConfig = {
      headers: header,
    }

    const res = await gameAxios.get<Response>(url, options)
    return res.data
  },
  post: async function post<Response = unknown, Request = unknown>(
    url: string,
    body?: Request,
    header?: AxiosRequestConfig['headers'],
  ) {
    const options: AxiosRequestConfig = {
      headers: header,
    }

    const res = await gameAxios.post<Response>(url, body, options)
    return res.data
  },
}

// 인터셉터 설정
// authAxios.interceptors.request.use(
//   async (config: InternalAxiosRequestConfig) => {
//     const accessToken = useRecoilValue(accessTokenState);
//     const setAccessToken = useSetRecoilState(accessTokenState);

//     if (accessToken) {
//       config.headers = config.headers || {};
//       (
//         config.headers as AxiosRequestHeaders
//       ).Authorization = `Bearer ${accessToken}`;
//     }
//     // 없는 경우 리프레시 토큰으로 액세스 토큰 재발급
//     else {
//       try {
//         const refreshResponse = await http.post<ResponseAccessTokenType>(
//           'user-service/auth/token'
//         );
//         const newAccessToken = refreshResponse.accessToken;

//         if (newAccessToken) {
//           config.headers = config.headers || {};
//           (
//             config.headers as AxiosRequestHeaders
//           ).Authorization = `Bearer ${newAccessToken}`;

//           // 액세스 토큰을 전역에 저장
//           setAccessToken(refreshResponse);

//           // await setUserData();
//         }
//       } catch (error) {
//         console.error('액세스 토큰 재발급 실패:', error);
//         // 재발급 실패 시 로그아웃 등의 처리를 진행할 수 있습니다.
//         localStorage.removeItem('login');
//         // window.location.href = 'https://i9d211.p.ssafy.io/login';
//       }
//     }
//     return config;
//   },
//   (error) => {
//     localStorage.removeItem('login');
//     window.location.href = 'https://i9d211.p.ssafy.io/login';
//     return Promise.reject(error);
//   }
// );

export const authHttp = {
  get: async function get<Response = unknown>(
    url: string,
    header?: AxiosRequestConfig['headers'],
    params?: object,
  ) {
    const options: AxiosRequestConfig = {
      headers: header,
      params: params,
    }
    const res = await authAxios.get<Response>(url, options)
    return res.data
  },

  post: async function post<Response = unknown, Request = unknown>(
    url: string,
    body?: Request,
    header?: AxiosRequestConfig['headers'],
  ) {
    const options: AxiosRequestConfig = {
      headers: header,
    }

    const res = await authAxios.post<Response>(url, body, options)
    return res.data
  },

  put: async function put<Response = unknown, Request = unknown>(
    url: string,
    body?: Request,
    header?: AxiosRequestConfig['headers'],
  ) {
    const options: AxiosRequestConfig = {
      headers: header,
    }

    const res = await authAxios.put<Response>(url, body, options)
    return res.data
  },

  patch: async function patch<Response = unknown, Request = unknown>(
    url: string,
    body?: Request,
    header?: AxiosRequestConfig['headers'],
  ) {
    const options: AxiosRequestConfig = {
      headers: header,
    }

    const res = await authAxios.patch<Response>(url, body, options)
    return res.data
  },

  delete: async function axiosDelete<Response = unknown>(
    url: string,
    header?: AxiosRequestConfig['headers'],
  ) {
    const options: AxiosRequestConfig = {
      headers: header,
    }
    const res = await authAxios.delete<Response>(url, options)
    return res.data
  },
}
